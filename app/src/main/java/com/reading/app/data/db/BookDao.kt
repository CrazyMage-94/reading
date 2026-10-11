package com.reading.app.data.db

import androidx.room.*
import com.reading.app.data.model.Book
import com.reading.app.data.model.BookSource
import com.reading.app.data.model.Chapter
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY updatedAt DESC")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM books WHERE bookUrl = :url")
    suspend fun getBook(url: String): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(book: Book)

    @Delete
    suspend fun delete(book: Book)

    @Query("UPDATE books SET lastReadIndex = :index, updatedAt = :time WHERE bookUrl = :url")
    suspend fun updateProgress(url: String, index: Int, time: Long = System.currentTimeMillis())

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChapters(chapters: List<Chapter>)

    @Query("SELECT * FROM chapters WHERE bookUrl = :url ORDER BY `index` ASC")
    suspend fun getChapters(url: String): List<Chapter>

    @Query("UPDATE chapters SET isCached = 1 WHERE bookUrl = :url AND `index` = :idx")
    suspend fun markCached(url: String, idx: Int)
}

@Dao
interface SourceDao {
    @Query("SELECT * FROM book_sources WHERE enabled = 1")
    fun getEnabledSources(): Flow<List<BookSource>>

    @Query("SELECT * FROM book_sources WHERE enabled = 1")
    suspend fun getEnabledSourcesOnce(): List<BookSource>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(sources: List<BookSource>)

    @Query("UPDATE book_sources SET enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: String, enabled: Boolean)
}