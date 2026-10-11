package com.reading.app.ui.bookshelf

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reading.app.data.model.Book
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookshelfViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = (app as com.reading.app.App).repository
    val books = repo.shelfBooks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    fun deleteBook(book: Book) = viewModelScope.launch { repo.removeFromShelf(book) }
}