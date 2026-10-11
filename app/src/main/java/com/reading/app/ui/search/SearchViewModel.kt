package com.reading.app.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reading.app.data.model.Book
import com.reading.app.data.source.SearchBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = (app as com.reading.app.App).repository
    val query = MutableStateFlow("")
    val results = MutableStateFlow<List<SearchBook>>(emptyList())
    val loading = MutableStateFlow(false)

    fun onQueryChange(q: String) { query.value = q }

    fun search() {
        val kw = query.value.trim()
        if (kw.isBlank()) return
        loading.value = true; results.value = emptyList()
        viewModelScope.launch {
            try {
                val map = repo.searchAll(kw)
                results.value = map.values.flatten()
            } finally { loading.value = false }
        }
    }

    fun addToShelf(item: SearchBook, done: () -> Unit) {
        viewModelScope.launch {
            val book = Book(
                bookUrl = item.bookUrl, name = item.name, author = item.author,
                coverUrl = item.cover, intro = item.intro
            )
            repo.addToShelf(book)
            done()
        }
    }
}