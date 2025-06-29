package com.example.newsapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
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
    val articles= viewModel.homePagingFlow.collectAsLazyPagingItems()
    val isRefreshing = articles.loadState.refresh is LoadState.Loading

    ArticleList(
        articles = articles,
        isRefreshing = isRefreshing,
        onArticleClick = onArticleClick,
        onRefresh = { articles.refresh() },
        modifier = modifier.fillMaxSize()
    )

}