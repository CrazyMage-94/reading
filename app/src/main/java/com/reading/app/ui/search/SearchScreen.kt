package com.reading.app.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reading.app.data.source.SearchBook

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onBack: () -> Unit, autoKeyword: String = "") {
    val vm: SearchViewModel = viewModel()
    val query by vm.query.collectAsState()
    val results by vm.results.collectAsState()
    val loading by vm.loading.collectAsState()
    val message by vm.message.collectAsState()
    val addedUrls by vm.addedUrls.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(autoKeyword) {
        if (autoKeyword.isNotBlank()) {
            vm.onQueryChange(autoKeyword)
            vm.search()
        }
    }
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            vm.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("搜书") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回") }
                }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = query,
                onValueChange = vm::onQueryChange,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                placeholder = { Text("输入书名或作者") },
                singleLine = true,
                trailingIcon = {
                    if (loading) com.reading.app.ui.common.LoadingIndicator(Modifier.size(24.dp))
                    else IconButton(onClick = vm::search) { Icon(Icons.Default.Search, "搜索") }
                }
            )
            Spacer(Modifier.height(8.dp))
            Text(
                if (results.isEmpty()) "输入关键词后搜索" else "共 ${results.size} 条结果",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                items(results) { item ->
                    SearchItem(
                        item = item,
                        added = item.bookUrl in addedUrls,
                        onClick = { vm.addToShelf(item, onAlready = {}, onSuccess = {}) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchItem(item: SearchBook, added: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.cover.isNotBlank()) {
            AsyncImage(item.cover, null, Modifier.size(width = 56.dp, height = 76.dp), contentScale = ContentScale.Crop)
            Spacer(Modifier.width(12.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(item.name, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 16.sp)
            if (item.author.isNotBlank()) Text(item.author, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (item.intro.isNotBlank()) Text(item.intro, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        IconButton(onClick = onClick, enabled = !added) {
            if (added) Icon(Icons.Default.Check, "已加入", tint = MaterialTheme.colorScheme.primary)
            else Icon(Icons.Default.Add, "加入书架")
        }
    }
}
