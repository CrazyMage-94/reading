package com.reading.app.data.source

import com.reading.app.data.model.BookSource

object DefaultSources {
    fun all(): List<BookSource> = listOf(
        BookSource(
            id = "xbiquge_la", name = "新笔趣阁",
            searchUrl = "https://www.xbiquge.la/modules/article/waps.php?searchkey={{key}}",
            charset = "UTF-8",
            ruleSearchBookList = "table.grid>tbody>tr",
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
            id = "biquwx_la", name = "笔趣屋",
            searchUrl = "https://www.biquwx.la/search.php?q={{key}}",
            charset = "UTF-8",
            ruleSearchBookList = ".result-item",
            ruleSearchName = ".result-item-title>a@text",
            ruleSearchAuthor = ".result-game-item-info-tag:nth-child(2)@text",
            ruleSearchCover = ".result-item-title>a>img@src",
            ruleSearchBookUrl = ".result-item-title>a@href",
            ruleBookIntro = "#intro",
            ruleBookCover = "#fmimg>img@src",
            ruleChapterList = "#list>dl>dd>a",
            ruleChapterName = "text",
            ruleChapterUrl = "href",
            ruleContent = "#content"
        ),
        BookSource(
            id = "quanben_io", name = "全本小说网",
            searchUrl = "https://www.quanben.io/n/{key}/",
            charset = "UTF-8",
            ruleSearchBookList = "#main .novellist>ul>li",
            ruleSearchName = "a@text",
            ruleSearchAuthor = "",
            ruleSearchCover = "",
            ruleSearchBookUrl = "a@href",
            ruleBookIntro = "#intro",
            ruleBookCover = "#fmimg>img@src",
            ruleChapterList = ".list>dd>a",
            ruleChapterName = "text",
            ruleChapterUrl = "href",
            ruleContent = "#content"
        ),
        BookSource(
            id = "piaotia_com", name = "飘天文学",
            searchUrl = "https://www.piaotia.com/search.php?q={{key}}",
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