package com.example.newsapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.data.model.Article
import com.example.newsapp.ui.HomeViewModel
import com.example.newsapp.ui.views.ArticleList

@Composable
fun SavedArticleScreen(
    viewModel: HomeViewModel,
    onArticleClick: (Article) -> Unit,
    onDeleteArticle: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val savedArticles = viewModel.savedArticlesPagingFlow.collectAsLazyPagingItems()
    val isRefreshing = savedArticles.loadState.refresh is LoadState.Loading
    Column(modifier = modifier.fillMaxSize()) {
        ArticleList(
            articles = savedArticles,
            isRefreshing = isRefreshing,
            onArticleClick = onArticleClick,
            onRefresh = { savedArticles.refresh() },
            swipeToDelete = true,
            onDelete = onDeleteArticle,
            modifier = Modifier.fillMaxSize()
        )
    }
}