package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.ui.HomeViewModel
import com.example.newsapp.ui.screens.ArticleDetailScreen
import com.example.newsapp.ui.screens.HomeScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.example.newsapp.ui.screens.SavedArticleScreen
import com.example.newsapp.ui.screens.SearchScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsAppTheme {
                AppNavGraph()
            }
        }
    }
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = modifier.padding(innerPadding)
        ) {
            composable("home") {
                BaseScreenScaffold(
                    title = "News Headlines",
                    showBackButton = false,
                    navController = navController,
                    showBottomBar = true
                ) { padding ->
                    HomeScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(padding),
                        onArticleClick = {
                            viewModel.selectArticle(it)
                            navController.navigate("detail")
                        }
                    )
                }
            }
            composable("search") {
                BaseScreenScaffold(
                    title = "Search Articles",
                    showBackButton = false,
                    navController = navController,
                    showBottomBar = true
                ) { padding ->
                    SearchScreen(
                        viewModel = viewModel,
                        onArticleClick = {
                            viewModel.selectArticle(it)
                            navController.navigate("detail")
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
            composable("detail") {
                val article = viewModel.selectedArticle.collectAsState().value
                val savedArticles = viewModel.savedArticles.collectAsState().value
                article?.let {
                    val isSaved = savedArticles.any { saved -> saved.url == it.url }

                    BaseScreenScaffold(
                        title = "Article Details",
                        showBackButton = true,
                        navController = navController,
                        showBottomBar = false,
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    if (isSaved) {
                                        viewModel.removeArticle(it)
                                    } else {
                                        viewModel.saveArticle(it)
                                    }
                                },
                                content = {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Default.Delete else Icons.Default.Add,
                                        contentDescription = if (isSaved) "Delete Article" else "Save Article"
                                    )
                                }
                            )
                        }
                    ) { padding ->
                        ArticleDetailScreen(
                            article = it,
                            onOpenInBrowser = { url ->
                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                context.startActivity(intent, null)
                            },
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
            composable("saved") {
                val savedArticles = viewModel.savedArticles.collectAsState().value
                BaseScreenScaffold(
                    title = "Saved Articles",
                    showBackButton = false,
                    navController = navController,
                    showBottomBar = true
                ) { padding ->
                    SavedArticleScreen(
                        articles = savedArticles,
                        onArticleClick = {
                            viewModel.selectArticle(it)
                            navController.navigate("detail")
                        },
                        onDeleteArticle = {
                            viewModel.removeArticle(it)
                        },
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", "Home", Icons.Default.Home),
        BottomNavItem("search", "Search", Icons.Default.Search),
        BottomNavItem("saved", "Saved", Icons.Default.Check)
    )
    NavigationBar {
        val currentRoute =
            navController.currentBackStackEntryFlow.collectAsState(null).value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

@Composable
fun BaseScreenScaffold(
    modifier: Modifier = Modifier,
    title: String? = null,
    showBackButton: Boolean = false,
    navController: NavHostController? = null,
    showBottomBar: Boolean = true,
    floatingActionButton: @Composable (() -> Unit) = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (title != null) {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = if (showBackButton && navController != null) {
                        {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    } else null,
                    backgroundColor = MaterialTheme.colorScheme.background
                )
            }
        },
        bottomBar = {
            if (showBottomBar && navController != null) {
                BottomNavigationBar(navController)
            }
        },
        modifier = modifier,
        floatingActionButton = floatingActionButton,
        content = content
    )
}


