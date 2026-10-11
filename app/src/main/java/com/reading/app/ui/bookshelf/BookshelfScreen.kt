package com.reading.app.ui.bookshelf

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.reading.app.data.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfScreen(onSearch: () -> Unit, onOpenBook: (String) -> Unit) {
    val vm: BookshelfViewModel = viewModel()
    val books by vm.books.collectAsState(initial = emptyList())
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("书架", fontSize = 20.sp) },
                actions = {
                    IconButton(onClick = onSearch) { Icon(Icons.Default.Search, "搜索") }
                }
            )
        }
    ) { padding ->
        if (books.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.MenuBook, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                    Spacer(Modifier.height(16.dp))
                    Text("书架空空如也", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("点击右上角搜索添加小说", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onSearch) { Text("去搜书") }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(books) { book ->
                    BookCard(book, onClick = { onOpenBook(book.bookUrl) }, onDelete = { vm.deleteBook(book) })
                }
            }
        }
    }
}

@Composable
fun BookCard(book: Book, onClick: () -> Unit, onDelete: () -> Unit) {
    var showDelete by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            Modifier.fillMaxWidth().aspectRatio(0.72f),
            contentAlignment = Alignment.Center
        ) {
            if (book.coverUrl.isNotBlank()) {
                AsyncImage(book.coverUrl, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } else {
                Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
                    Box(Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.Center) {
                        Text(book.name.take(4), fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(book.name, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp)
        Text("第${book.lastReadIndex + 1}章", maxLines = 1, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        DropdownMenu(expanded = showDelete, onDismissRequest = { showDelete = false }) {
            DropdownMenuItem(text = { Text("移出书架") }, onClick = { showDelete = false; onDelete() })
        }
    }
}