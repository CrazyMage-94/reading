package com.reading.app.ui.search

import android.app.Application
import android.util.Log
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
    val message = MutableStateFlow<String?>(null)
    val addedUrls = MutableStateFlow<Set<String>>(emptySet())

    fun onQueryChange(q: String) { query.value = q }

    fun search() {
        val kw = query.value.trim()
        if (kw.isBlank()) { message.value = "请输入书名或作者"; return }
        loading.value = true; results.value = emptyList()
        viewModelScope.launch {
            try {
                val map = repo.searchAll(kw)
                results.value = map.values.flatten()
                if (results.value.isEmpty()) message.value = "未找到结果，书源可能失效，试试换个关键词"
            } catch (e: Exception) {
                Log.e("SearchVM", "search failed", e)
                message.value = "搜索失败：${e.message ?: "网络错误"}"
            } finally { loading.value = false }
        }
    }

    fun addToShelf(item: SearchBook, onAlready: () -> Unit, onSuccess: () -> Unit) {
        if (item.bookUrl in addedUrls.value) { message.value = "已在书架中"; onAlready(); return }
        viewModelScope.launch {
            try {
                if (repo.getBook(item.bookUrl) != null) {
                    addedUrls.value = addedUrls.value + item.bookUrl
                    message.value = "这本书已在书架中"
                    onAlready()
                    return@launch
                }
                repo.addToShelf(
                    Book(
                        bookUrl = item.bookUrl, name = item.name, author = item.author,
                        coverUrl = item.cover, intro = item.intro
                    )
                )
                addedUrls.value = addedUrls.value + item.bookUrl
                message.value = "已加入书架：${item.name}"
                onSuccess()
            } catch (e: Exception) {
                Log.e("SearchVM", "addToShelf failed", e)
                message.value = "加入失败：${e.message ?: "未知错误"}"
            }
        }
    }

    fun clearMessage() { message.value = null }
}