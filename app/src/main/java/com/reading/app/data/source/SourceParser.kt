package com.reading.app.data.source

import android.util.Log
import com.reading.app.data.model.BookSource
import com.reading.app.data.model.Chapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.concurrent.TimeUnit

data class SearchBook(
    var name: String = "", var author: String = "", var cover: String = "",
    var bookUrl: String = "", var intro: String = "", var sourceId: String = ""
)

class SourceParser {
    companion object { const val TAG = "SourceParser" }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .followRedirects(true)
        .addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                .build())
        }
        .build()

    suspend fun fetchHtml(url: String, charset: String = "UTF-8", postBody: Pair<String, String>? = null): String =
        withContext(Dispatchers.IO) {
            try {
                val builder = Request.Builder().url(url).header("Referer", url)
                if (postBody != null) {
                    val form = FormBody.Builder().add(postBody.first, postBody.second).build()
                    builder.post(form)
                }
                val resp = client.newCall(builder.build()).execute()
                try {
                    val bytes = resp.body?.bytes() ?: ByteArray(0)
                    String(bytes, charset(charset))
                } finally { resp.close() }
            } catch (e: Exception) {
                Log.w(TAG, "fetch failed: $url -> ${e.message}")
                ""
            }
        }

    suspend fun search(source: BookSource, keyword: String): List<SearchBook> = withContext(Dispatchers.IO) {
        try {
            var url = source.searchUrl
            var postBody: Pair<String, String>? = null
            if (url.startsWith("POST,")) {
                postBody = "searchkey" to keyword
                url = url.removePrefix("POST,")
            }
            val encoded = java.net.URLEncoder.encode(keyword, source.charset)
            url = url.replace("{{key}}", encoded).replace("{key}", encoded)
            val html = fetchHtml(url, source.charset, postBody)
            if (html.isBlank()) {
                Log.w(TAG, "[${source.name}] search: empty html")
                return@withContext emptyList()
            }
            val doc = Jsoup.parse(html, url)
            val results = doc.select(source.ruleSearchBookList).mapNotNull { el ->
                val name = resolveText(el, source.ruleSearchName)
                if (name.isBlank()) return@mapNotNull null
                SearchBook(
                    name = name,
                    author = resolveText(el, source.ruleSearchAuthor),
                    cover = absolutize(resolveAttr(el, source.ruleSearchCover, el.baseUri()), el.baseUri()),
                    bookUrl = absolutize(resolveAttr(el, source.ruleSearchBookUrl, el.baseUri()), el.baseUri()),
                    sourceId = source.id
                )
            }
            Log.i(TAG, "[${source.name}] search '${keyword}' -> ${results.size} results")
            results
        } catch (e: Exception) {
            Log.w(TAG, "[${source.name}] search error: ${e.message}")
            emptyList()
        }
    }

    suspend fun getBookInfo(source: BookSource, bookUrl: String): SearchBook = withContext(Dispatchers.IO) {
        val html = fetchHtml(bookUrl, source.charset)
        val doc = Jsoup.parse(html, bookUrl)
        val cover = doc.selectFirst(source.ruleBookCover)?.attr(
            source.ruleBookCover.substringAfterLast("@", "src")
        ) ?: doc.selectFirst("meta[property=og:image]")?.attr("content") ?: ""
        SearchBook(
            name = doc.selectFirst("h1")?.text() ?: "",
            author = "",
            cover = absolutize(cover, bookUrl),
            bookUrl = bookUrl,
            intro = resolveText(doc, source.ruleBookIntro).take(500)
        )
    }

    suspend fun getChapters(source: BookSource, bookUrl: String): List<Chapter> = withContext(Dispatchers.IO) {
        val html = fetchHtml(bookUrl, source.charset)
        if (html.isBlank()) return@withContext emptyList()
        var doc = Jsoup.parse(html, bookUrl)
        var pageUrl = bookUrl
        var elements = doc.select(source.ruleChapterList)
        // 目录在独立页面（如 list.html）：自动跟随
        if (elements.isEmpty()) {
            val listLink = Regex("href=\"([^\"]*list\\.html)\"").find(html)?.groupValues?.get(1)
            if (listLink != null) {
                pageUrl = absolutize(listLink, bookUrl)
                Log.i(TAG, "[${source.name}] chapters on separate page: $pageUrl")
                val listHtml = fetchHtml(pageUrl, source.charset)
                doc = Jsoup.parse(listHtml, pageUrl)
                elements = doc.select(source.ruleChapterList)
            }
        }
        var chapters = buildChapters(source, bookUrl, elements, stripAmp = false)
        // 缺口检测：URL 编号跨度远大于条目数 → 目录被缩略，尝试 AMP 完整目录
        if (hasChapterGap(chapters)) {
            val ampUrl = findAmpListUrl(doc, pageUrl)
            if (ampUrl != null) {
                Log.i(TAG, "[${source.name}] gap detected, trying AMP full list: $ampUrl")
                val ampHtml = fetchHtml(ampUrl, source.charset)
                if (ampHtml.isNotBlank()) {
                    val ampDoc = Jsoup.parse(ampHtml, ampUrl)
                    val ampChapters = buildChapters(source, bookUrl, ampDoc.select(source.ruleChapterList), stripAmp = true)
                    if (ampChapters.size > chapters.size) {
                        Log.i(TAG, "[${source.name}] AMP full list: ${ampChapters.size} chapters")
                        chapters = ampChapters
                    }
                }
            }
        }
        Log.i(TAG, "[${source.name}] chapters total: ${chapters.size}")
        chapters
    }

    private fun buildChapters(source: BookSource, bookUrl: String, elements: org.jsoup.select.Elements, stripAmp: Boolean): List<Chapter> =
        elements.mapIndexedNotNull { i, el ->
            val title = resolveText(el, source.ruleChapterName)
            var href = absolutize(resolveAttr(el, source.ruleChapterUrl, el.baseUri()), el.baseUri())
            if (stripAmp) href = href.replace("/amp/", "/")
            if (title.isBlank() || href.isBlank()) null
            else Chapter(bookUrl, i, title = title, url = href)
        }

    private fun hasChapterGap(chapters: List<Chapter>): Boolean {
        if (chapters.size < 10) return false
        val ids = chapters.mapNotNull { Regex("(\\d+)\\.html").find(it.url)?.groupValues?.get(1)?.toLongOrNull() }.sorted()
        if (ids.size < 10) return false
        val span = ids.last() - ids.first() + 1
        return span > ids.size * 2 + 20
    }

    private fun findAmpListUrl(doc: org.jsoup.nodes.Document, pageUrl: String): String? {
        doc.selectFirst("link[rel=amphtml]")?.attr("href")?.let { return absolutize(it, pageUrl) }
        return try {
            val u = java.net.URI(pageUrl)
            "${u.scheme}://${u.host}/amp${u.path}"
        } catch (e: Exception) { null }
    }
    suspend fun getContent(source: BookSource, chapterUrl: String): String = withContext(Dispatchers.IO) {
        val html = fetchHtml(chapterUrl, source.charset)
        if (html.isBlank()) return@withContext ""
        val doc = Jsoup.parse(html, chapterUrl)
        val contentEl = doc.selectFirst(source.ruleContent) ?: run {
            Log.w(TAG, "[${source.name}] content selector miss: $chapterUrl")
            return@withContext ""
        }
        contentEl.select("script,style,ins,iframe,.txtnav,.adsbygoogle,[id*=ad],[class*=ad],[class*=banner]").remove()
        // 按段落提取：优先 <p> 标签，否则 <br> 转换行（wholeText 会丢失块级换行，不能用）
        val pEls = contentEl.select("p")
        val rawLines: List<String> = if (pEls.size > 1) {
            pEls.map { it.text() }
        } else {
            val brHtml = contentEl.html().replace(Regex("(?i)<br\\s*/?\\s*>"), "\n")
            Jsoup.parse(brHtml).wholeText().split("\n")
        }
        rawLines
            .map { it.replace(Regex("[ \\t\\x0B\\f\\r]+"), " ").trim() }
            .filter {
                it.isNotBlank() && !it.contains("本章未完") && !it.contains("点击下一页") &&
                    !it.contains("最新章节") && !it.contains("章节内容开始")
            }
            .joinToString("\n")
    }

    private fun resolveText(el: Element, rule: String): String {
        if (rule.isBlank()) return ""
        return when {
            rule.startsWith("@") -> el.ownText()
            rule.contains("@") -> {
                val parts = rule.split("@")
                val target = el.selectFirst(parts[0])
                target?.let { if (parts[1] == "text") it.text() else it.attr(parts[1]) } ?: ""
            }
            else -> el.selectFirst(rule)?.text() ?: ""
        }
    }

    private fun resolveAttr(el: Element, rule: String, baseUri: String): String {
        if (rule.isBlank()) return ""
        return when {
            rule == "href" -> el.attr("href")
            rule == "src" -> el.attr("src")
            rule.contains("@") -> {
                val parts = rule.split("@")
                el.selectFirst(parts[0])?.attr(parts[1]) ?: ""
            }
            else -> el.selectFirst(rule)?.attr(rule.substringAfterLast("@")) ?: ""
        }
    }

    private fun absolutize(url: String, base: String): String {
        if (url.isBlank()) return ""
        if (url.startsWith("//")) return "https:$url"
        return try { java.net.URL(java.net.URL(base), url).toString() } catch (e: Exception) { url }
    }

    private fun charset(name: String) = try { java.nio.charset.Charset.forName(name) } catch (e: Exception) { Charsets.UTF_8 }
}