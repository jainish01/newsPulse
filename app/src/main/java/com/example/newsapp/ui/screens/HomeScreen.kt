package com.example.newsapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.data.model.Article
import com.example.newsapp.ui.HomeViewModel
import com.example.newsapp.ui.views.ArticleList
import com.example.newsapp.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onArticleClick: (Article) -> Unit
) {
    val state = viewModel.homeArticles.collectAsState().value
    val isRefreshing = state is Resource.Loading

    ArticleList(
        state = state,
        isRefreshing = isRefreshing,
        onArticleClick = onArticleClick,
        onRefresh = { viewModel.loadHeadlines() },
        modifier = modifier.fillMaxSize()
    )

}