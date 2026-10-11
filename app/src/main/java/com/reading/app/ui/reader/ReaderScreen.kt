package com.reading.app.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reading.app.ui.common.LoadingIndicator
import kotlinx.coroutines.launch

private enum class ReaderTheme(val bg: Color, val text: Color) {
    PAPER(Color(0xFFF5F1E8), Color(0xFF2C1810)),
    GREEN(Color(0xFFCCE8CF), Color(0xFF1B3A1B)),
    NIGHT(Color(0xFF121212), Color(0xFF9E9E9E)),
    BLUE(Color(0xFFD6E4F0), Color(0xFF1A2A3A))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(bookUrl: String, onBack: () -> Unit) {
    val vm: ReaderViewModel = viewModel()
    LaunchedEffect(bookUrl) { vm.load(bookUrl, -1) }
    val state by vm.state.collectAsState()
    var theme by remember { mutableStateOf(ReaderTheme.PAPER) }
    var fontSize by remember { mutableStateOf(18f) }
    var showMenu by remember { mutableStateOf(false) }
    var showChapters by remember { mutableStateOf(false) }
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    var viewportH by remember { mutableStateOf(0) }

    // 换章后回到顶部
    LaunchedEffect(state.currentIndex) { scroll.scrollTo(0) }

    fun nextPage() {
        val pageStep = (viewportH - 80).coerceAtLeast(200)
        if (scroll.value >= scroll.maxValue - 40) {
            vm.next()
        } else {
            scope.launch { scroll.animateScrollTo((scroll.value + pageStep).coerceAtMost(scroll.maxValue)) }
        }
    }
    fun prevPage() {
        val pageStep = (viewportH - 80).coerceAtLeast(200)
        if (scroll.value <= 40) {
            vm.prev()
        } else {
            scope.launch { scroll.animateScrollTo((scroll.value - pageStep).coerceAtLeast(0)) }
        }
    }

    Column(Modifier.fillMaxSize().background(theme.bg)) {
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
        Box(
            Modifier.weight(1f).onSizeChanged { viewportH = it.height }
        ) {
            if (state.loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { LoadingIndicator() }
            } else if (state.error != null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.error ?: "", color = theme.text, textAlign = TextAlign.Center, modifier = Modifier.padding(24.dp))
                        Button(onClick = { vm.goTo(state.currentIndex) }) { Text("重试") }
                    }
                }
            } else {
                Column(
                    Modifier.fillMaxSize()
                        .verticalScroll(scroll)
                        .pointerInput(state.content) {
                            detectTapGestures { offset ->
                                val w = size.width
                                when {
                                    offset.x < w / 3f -> prevPage()
                                    offset.x > w * 2 / 3f -> nextPage()
                                    else -> showMenu = !showMenu
                                }
                            }
                        }
                        .pointerInput(state.content) {
                            var accum = 0f
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (accum < -120f) nextPage()
                                    else if (accum > 120f) prevPage()
                                    accum = 0f
                                }
                            ) { change, amount ->
                                change.consume()
                                accum += amount
                            }
                        }
                        .padding(horizontal = 20.dp)
                ) {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        state.chapters.getOrNull(state.currentIndex)?.title ?: "",
                        fontSize = 20.sp, color = theme.text, modifier = Modifier.padding(bottom = 16.dp)
                    )
                    state.content.split("\n").forEach { line ->
                        Text(
                            "　　${line.trim()}", fontSize = fontSize.sp, lineHeight = (fontSize * 1.8f).sp,
                            color = theme.text, modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                    Spacer(Modifier.height(32.dp))
                    Text(
                        "本章节完 · 下一章：${state.chapters.getOrNull(state.currentIndex + 1)?.title ?: "没有了"}",
                        fontSize = 14.sp, color = theme.text.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth().clickable { vm.next() }.padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(48.dp))
                }
            }

        }
        if (showMenu) {
            Column(Modifier.fillMaxWidth().background(theme.bg).padding(vertical = 8.dp)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("A-", color = theme.text, fontSize = 18.sp, modifier = Modifier.clickable { if (fontSize > 12f) fontSize -= 2f })
                    Text("字号 ${fontSize.toInt()}", color = theme.text, fontSize = 14.sp)
                    Text("A+", color = theme.text, fontSize = 22.sp, modifier = Modifier.clickable { if (fontSize < 32f) fontSize += 2f })
                }
                Spacer(Modifier.height(8.dp))
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
                Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = vm::prev, enabled = state.currentIndex > 0) { Text("上一章", color = theme.text) }
                    Text(
                        "${state.currentIndex + 1}/${state.chapters.size}",
                        color = theme.text, fontSize = 14.sp, modifier = Modifier.padding(top = 14.dp)
                    )
                    TextButton(onClick = vm::next, enabled = state.currentIndex < state.chapters.size - 1) { Text("下一章", color = theme.text) }
                }
            }
        }
    }
    if (showChapters) {
        ModalBottomSheet(onDismissRequest = { showChapters = false }) {
            Column {
                Text("目录（${state.chapters.size}章）", Modifier.padding(16.dp), fontSize = 16.sp)
                androidx.compose.foundation.lazy.LazyColumn(Modifier.heightIn(max = 500.dp)) {
                    items(state.chapters.size) { i ->
                        Text(
                            state.chapters[i].title,
                            Modifier.fillMaxWidth().clickable { showChapters = false; vm.goTo(i) }.padding(horizontal = 16.dp, vertical = 10.dp),
                            fontSize = 14.sp,
                            color = if (i == state.currentIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}