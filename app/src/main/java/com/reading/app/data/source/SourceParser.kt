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
    var bookUrl: String = "", var intro: String = ""
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
                    bookUrl = absolutize(resolveAttr(el, source.ruleSearchBookUrl, el.baseUri()), el.baseUri())
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
        var elements = doc.select(source.ruleChapterList)
        // 目录在独立页面（如 list.html）：自动跟随
        if (elements.isEmpty()) {
            val listLink = Regex("href=\"([^\"]*list\\.html)\"").find(html)?.groupValues?.get(1)
            if (listLink != null) {
                val listUrl = absolutize(listLink, bookUrl)
                Log.i(TAG, "[${source.name}] chapters on separate page: $listUrl")
                val listHtml = fetchHtml(listUrl, source.charset)
                doc = Jsoup.parse(listHtml, listUrl)
                elements = doc.select(source.ruleChapterList)
            }
        }
        Log.i(TAG, "[${source.name}] chapters: ${elements.size} found")
        elements.mapIndexedNotNull { i, el ->
            val title = resolveText(el, source.ruleChapterName)
            val href = absolutize(resolveAttr(el, source.ruleChapterUrl, el.baseUri()), el.baseUri())
            if (title.isBlank() || href.isBlank()) null
            else Chapter(bookUrl, i, title = title, url = href)
        }
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
        contentEl.wholeText()
            .replace(Regex("\\u00a0|&nbsp;"), " ")
            .replace(Regex("[ \\t\\x0B\\f\\r]+"), " ")
            .replace(Regex("\\n{3,}"), "\n\n")
            .split("\n")
            .map { it.trim() }
            .filter { it.isNotBlank() && !it.contains("本章未完") && !it.contains("点击下一页") && !it.contains("最新章节") }
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