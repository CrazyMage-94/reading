package com.reading.app.ui.reader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reading.app.data.model.Book
import com.reading.app.data.model.BookSource
import com.reading.app.data.model.Chapter
import com.reading.app.data.source.SourceParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

data class ReaderState(
    val book: Book? = null,
    val chapters: List<Chapter> = emptyList(),
    val currentIndex: Int = 0,
    val content: String = "",
    val loading: Boolean = false,
    val error: String? = null
)

class ReaderViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = (app as com.reading.app.App).repository
    private val parser = SourceParser()
    private var source: BookSource? = null

    val state = MutableStateFlow(ReaderState())

    fun load(bookUrl: String, startIndex: Int) {
        state.value = state.value.copy(loading = true)
        viewModelScope.launch {
            try {
                val book = repo.getBook(bookUrl) ?: return@launch
                val src = repo.getEnabledSources().find { it.id == book.sourceId } ?: repo.getEnabledSources().firstOrNull()
                source = src ?: return@launch
                var chapters = repo.loadChapters(bookUrl)
                if (chapters.isEmpty()) {
                    chapters = repo.getChapters(src, bookUrl)
                    repo.saveChapters(chapters)
                }
                val idx = if (startIndex in chapters.indices) startIndex else book.lastReadIndex.coerceIn(0, (chapters.size - 1).coerceAtLeast(0))
                state.value = ReaderState(book = book, chapters = chapters, currentIndex = idx)
                loadContent(chapters.getOrNull(idx)?.url ?: "")
            } catch (e: Exception) {
                state.value = state.value.copy(loading = false, error = e.message)
            }
        }
    }

    fun loadContent(url: String) {
        val src = source ?: return
        state.value = state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                val text = parser.getContent(src, url)
                state.value = state.value.copy(content = text, loading = false)
            } catch (e: Exception) {
                state.value = state.value.copy(loading = false, error = "加载失败：${e.message}")
            }
        }
    }

    fun goTo(index: Int) {
        val ch = state.value.chapters.getOrNull(index) ?: return
        state.value = state.value.copy(currentIndex = index)
        viewModelScope.launch { state.value.book?.let { repo.updateProgress(it.bookUrl, index) } }
        loadContent(ch.url)
    }

    fun next() { goTo(state.value.currentIndex + 1) }
    fun prev() { goTo(state.value.currentIndex - 1) }
}