package com.reading.app.data.source

import com.reading.app.data.model.BookSource

object DefaultSources {
    fun all(): List<BookSource> = listOf(
        BookSource(
            id = "quanben_io", name = "全本小说网",
            searchUrl = "https://www.quanben.io/index.php?c=book&a=search&keywords={{key}}",
            charset = "UTF-8",
            ruleSearchBookList = "div.list2",
            ruleSearchName = "h3>a@text",
            ruleSearchAuthor = "span[itemprop=author]@text",
            ruleSearchCover = "img@src",
            ruleSearchBookUrl = "h3>a@href",
            ruleBookIntro = "meta[name=description]@content",
            ruleBookCover = "img[itemprop=image]@src",
            ruleChapterList = "ul.list3>li",
            ruleChapterName = "a@text",
            ruleChapterUrl = "a@href",
            ruleContent = "#content"
        ),
        BookSource(
            id = "xbiquge_la", name = "新笔趣阁",
            searchUrl = "POST,https://www.xbiquge.la/modules/article/waps.php",
            charset = "UTF-8",
            ruleSearchBookList = "#result>table>tbody>tr",
            ruleSearchName = "td:nth-child(2)>a@text",
            ruleSearchAuthor = "td:nth-child(3)@text",
            ruleSearchCover = "",
            ruleSearchBookUrl = "td:nth-child(2)>a@href",
            ruleBookIntro = "#intro",
            ruleBookCover = "#fmimg>img@src",
            ruleChapterList = "#list>dl>dd>a",
            ruleChapterName = "text",
            ruleChapterUrl = "href",
            ruleContent = "#content"
        ),
        BookSource(
            id = "xbiquge_bz", name = "笔趣阁BZ",
            searchUrl = "POST,https://www.xbiquge.bz/modules/article/waps.php",
            charset = "UTF-8",
            ruleSearchBookList = "#result>table>tbody>tr",
            ruleSearchName = "td:nth-child(2)>a@text",
            ruleSearchAuthor = "td:nth-child(3)@text",
            ruleSearchCover = "",
            ruleSearchBookUrl = "td:nth-child(2)>a@href",
            ruleBookIntro = "#intro",
            ruleBookCover = "#fmimg>img@src",
            ruleChapterList = "#list>dl>dd>a",
            ruleChapterName = "text",
            ruleChapterUrl = "href",
            ruleContent = "#content"
        ),
        BookSource(
            id = "piaotia_com", name = "飘天文学",
            searchUrl = "POST,https://www.piaotia.com/modules/article/search.php",
            charset = "GBK",
            ruleSearchBookList = ".novelslist2>li",
            ruleSearchName = ".s2>a@text",
            ruleSearchAuthor = ".s4@text",
            ruleSearchCover = "",
            ruleSearchBookUrl = ".s2>a@href",
            ruleBookIntro = "#intro",
            ruleBookCover = "#fmimg>img@src",
            ruleChapterList = "#list>dl>dd>a",
            ruleChapterName = "text",
            ruleChapterUrl = "href",
            ruleContent = "#content"
        )
    )
}