package com.reading.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.reading.app.ui.bookshelf.BookshelfScreen
import com.reading.app.ui.reader.ReaderScreen
import com.reading.app.ui.search.SearchScreen
import com.reading.app.ui.theme.ReadingAppTheme
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as App
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try { app.repository.initDefaultSources() } catch (_: Exception) { }
        }
        setContent {
            ReadingAppTheme {
                val navController = rememberNavController()
                val autoKw = intent?.getStringExtra("auto_search")
                LaunchedEffect(autoKw) {
                    if (!autoKw.isNullOrBlank()) navController.navigate("search/$autoKw")
                }
                NavHost(navController, startDestination = "bookshelf") {
                    composable("bookshelf") {
                        BookshelfScreen(
                            onSearch = { navController.navigate("search") },
                            onOpenBook = { url -> navController.navigate("reader/${URLEncoder.encode(url, "UTF-8")}") }
                        )
                    }
                    composable("search") {
                        SearchScreen(onBack = { navController.popBackStack() })
                    }
                    composable(
                        "search/{keyword}",
                        arguments = listOf(navArgument("keyword") { type = NavType.StringType })
                    ) { backStack ->
                        val kw = URLDecoder.decode(backStack.arguments?.getString("keyword") ?: "", "UTF-8")
                        SearchScreen(onBack = { navController.popBackStack() }, autoKeyword = kw)
                    }
                    composable(
                        "reader/{bookUrl}",
                        arguments = listOf(navArgument("bookUrl") { type = NavType.StringType })
                    ) { backStack ->
                        val url = URLDecoder.decode(backStack.arguments?.getString("bookUrl") ?: "", "UTF-8")
                        ReaderScreen(bookUrl = url, onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
