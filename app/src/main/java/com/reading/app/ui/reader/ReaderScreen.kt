package com.reading.app.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

private enum class ReaderTheme(val bg: Color, val text: Color) {
    PAPER(Color(0xFFF5F1E8), Color(0xFF2C1810)),
    GREEN(Color(0xFFCCE8CF), Color(0xFF1B3A1B)),
    NIGHT(Color(0xFF121212), Color(0xFF9E9E9E)),
    BLUE(Color(0xFFD6E4F0), Color(0xFF1A2A3A))
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ReaderScreen(bookUrl: String, onBack: () -> Unit) {
    val vm: ReaderViewModel = viewModel()
    LaunchedEffect(bookUrl) { vm.load(bookUrl, 0) }
    val state by vm.state.collectAsState()
    var theme by remember { mutableStateOf(ReaderTheme.PAPER) }
    var fontSize by remember { mutableStateOf(18f) }
    var showMenu by remember { mutableStateOf(false) }
    var showChapters by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().background(theme.bg)) {
        // 顶栏
        if (showMenu) {
            Row(
                Modifier.fillMaxWidth().background(theme.bg).padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回", tint = theme.text) }
                Text(state.book?.name ?: "阅读", Modifier.weight(1f), color = theme.text, fontSize = 16.sp, maxLines = 1)
                IconButton(onClick = { showChapters = true }) { Icon(Icons.AutoMirrored.Filled.List, "目录", tint = theme.text) }
            }
        }
        // 正文
        Box(Modifier.weight(1f)) {
            if (state.loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            } else if (state.error != null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.error ?: "", color = theme.text, textAlign = TextAlign.Center, modifier = Modifier.padding(24.dp))
                        Button(onClick = { vm.goTo(state.currentIndex) }) { Text("重试") }
                    }
                }
            } else {
                Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(horizontal = 20.dp)) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        state.chapters.getOrNull(state.currentIndex)?.title ?: "",
                        fontSize = 20.sp, color = theme.text, modifier = Modifier.padding(bottom = 16.dp)
                    )
                    state.content.split("\n").forEach { line ->
                        Text(
                            line.trim(), fontSize = fontSize.sp, lineHeight = (fontSize * 1.7f).sp,
                            color = theme.text, modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                    Spacer(Modifier.height(32.dp))
                    // 上一章/下一章
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        if (state.currentIndex > 0) {
                            TextButton(onClick = vm::prev) { Text("上一章", color = theme.text) }
                        }
                        TextButton(onClick = vm::next) { Text("下一章", color = theme.text) }
                    }
                    Spacer(Modifier.height(48.dp))
                }
            }
            // 点击中间区域切换菜单
            Box(
                Modifier.fillMaxSize().pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { showMenu = !showMenu }
                    )
                }
            )
        }
        // 底部菜单
        if (showMenu) {
            Column(Modifier.fillMaxWidth().background(theme.bg).padding(vertical = 8.dp)) {
                // 字体大小
                Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("A-", color = theme.text, fontSize = 18.sp, modifier = Modifier.clickable { if (fontSize > 12f) fontSize -= 2f })
                    Text("字号 ${fontSize.toInt()}", color = theme.text, fontSize = 14.sp)
                    Text("A+", color = theme.text, fontSize = 22.sp, modifier = Modifier.clickable { if (fontSize < 32f) fontSize += 2f })
                }
                Spacer(Modifier.height(8.dp))
                // 主题
                Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ReaderTheme.entries.forEach { t ->
                        Surface(
                            color = t.bg, modifier = Modifier.size(32.dp),
                            shape = MaterialTheme.shapes.small,
                            border = if (t == theme) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                            onClick = { theme = t }
                        ) { }
                    }
                }
                Spacer(Modifier.height(8.dp))
                // 翻章
                Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = vm::prev, enabled = state.currentIndex > 0) { Text("上一章", color = theme.text) }
                    TextButton(onClick = vm::next, enabled = state.currentIndex < state.chapters.size - 1) { Text("下一章", color = theme.text) }
                }
            }
        }
    }
    // 章节目录 BottomSheet
    if (showChapters) {
        ModalBottomSheet(onDismissRequest = { showChapters = false }) {
            Column {
                Text("目录（${state.chapters.size}章）", Modifier.padding(16.dp), fontSize = 16.sp)
                LazyColumnM(
                    chapters = state.chapters,
                    current = state.currentIndex,
                    onSelect = { idx -> showChapters = false; vm.goTo(idx) }
                )
            }
        }
    }
}

@Composable
private fun LazyColumnM(chapters: List<com.reading.app.data.model.Chapter>, current: Int, onSelect: (Int) -> Unit) {
    androidx.compose.foundation.lazy.LazyColumn(Modifier.heightIn(max = 500.dp)) {
        items(chapters.size) { i ->
            Text(
                chapters[i].title,
                Modifier.fillMaxWidth().clickable { onSelect(i) }.padding(horizontal = 16.dp, vertical = 10.dp),
                fontSize = 14.sp,
                color = if (i == current) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}