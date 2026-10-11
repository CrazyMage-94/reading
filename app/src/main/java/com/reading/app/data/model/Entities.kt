package com.reading.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val bookUrl: String,
    var name: String = "",
    var author: String = "",
    var coverUrl: String = "",
    var intro: String = "",
    var sourceId: String = "",
    var totalChapters: Int = 0,
    var lastReadIndex: Int = 0,
    var lastReadOffset: Int = 0,
    var addedAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chapters", primaryKeys = ["bookUrl", "index"])
data class Chapter(
    val bookUrl: String,
    val index: Int,
    var title: String = "",
    var url: String = "",
    var isCached: Boolean = false
)

@Entity(tableName = "book_sources")
data class BookSource(
    @PrimaryKey val id: String,
    var name: String,
    var searchUrl: String,
    var charset: String = "UTF-8",
    var ruleSearchBookList: String,
    var ruleSearchName: String,
    var ruleSearchAuthor: String,
    var ruleSearchCover: String,
    var ruleSearchBookUrl: String,
    var ruleBookIntro: String,
    var ruleBookCover: String,
    var ruleChapterList: String,
    var ruleChapterName: String,
    var ruleChapterUrl: String,
    var ruleContent: String,
    var enabled: Boolean = true
)