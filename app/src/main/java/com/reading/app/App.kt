package com.reading.app

import android.app.Application
import com.reading.app.data.db.AppDatabase
import com.reading.app.data.repo.BookRepository

class App : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val repository: BookRepository by lazy { BookRepository(database.bookDao(), database.sourceDao()) }
}