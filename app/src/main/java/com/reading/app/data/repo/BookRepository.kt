package com.reading.app.data.repo

import com.reading.app.data.db.BookDao
import com.reading.app.data.db.SourceDao
import com.reading.app.data.model.Book
import com.reading.app.data.model.BookSource
import com.reading.app.data.model.Chapter
import com.reading.app.data.source.SourceParser
import com.reading.app.data.source.SearchBook
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao, private val sourceDao: SourceDao) {
    private val parser = SourceParser()

    fun getAllBooks(): Flow<List<Book>> = bookDao.getAllBooks()

    suspend fun initDefaultSources() {
        try {
            val defaults = com.reading.app.data.source.DefaultSources.all()
            sourceDao.upsertAll(defaults)
            // 删除已下线的旧源
            val keepIds = defaults.map { it.id }.toSet()
            sourceDao.getEnabledSourcesOnce().filter { it.id !in keepIds }.forEach { sourceDao.delete(it.id) }
        } catch (_: Exception) { }
    }

    suspend fun getEnabledSources(): List<BookSource> = sourceDao.getEnabledSourcesOnce()

    suspend fun searchAll(keyword: String): Map<BookSource, List<SearchBook>> {
        val sources = getEnabledSources()
        val result = mutableMapOf<BookSource, List<SearchBook>>()
        sources.forEach { source ->
            try {
                val books = parser.search(source, keyword)
                if (books.isNotEmpty()) result[source] = books
            } catch (_: Exception) { }
        }
        return result
    }

    suspend fun getBookInfo(source: BookSource, bookUrl: String): SearchBook = parser.getBookInfo(source, bookUrl)

    suspend fun getChapters(source: BookSource, bookUrl: String): List<Chapter> = parser.getChapters(source, bookUrl)

    suspend fun getContent(source: BookSource, chapterUrl: String): String = parser.getContent(source, chapterUrl)

    suspend fun addToShelf(book: Book) = bookDao.upsert(book)
    suspend fun removeFromShelf(book: Book) = bookDao.delete(book)
    fun shelfBooks(): Flow<List<Book>> = bookDao.getAllBooks()
    suspend fun getBook(url: String) = bookDao.getBook(url)
    suspend fun saveChapters(chapters: List<Chapter>) = bookDao.upsertChapters(chapters)
    suspend fun loadChapters(url: String) = bookDao.getChapters(url)
    suspend fun updateProgress(url: String, index: Int) = bookDao.updateProgress(url, index)
    suspend fun markCached(url: String, idx: Int) = bookDao.markCached(url, idx)
}