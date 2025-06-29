package com.example.newsapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.data.model.Article
import com.example.newsapp.ui.HomeViewModel
import com.example.newsapp.ui.views.ArticleList

@Composable
fun SearchScreen(viewModel: HomeViewModel, onArticleClick: (Article) -> Unit, modifier: Modifier = Modifier) {
    val serachArticles = viewModel.searchPagingFlow.collectAsLazyPagingItems()
    val query by viewModel.searchQuery.collectAsState()
    Column(modifier.fillMaxSize()) {

        OutlinedTextField(
            value = query,
            onValueChange = {
                viewModel.updateSearchQuery(it)
            },
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            label = { Text("Search articles...") },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (query.isBlank()){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Start typing to search news articles.")
            }
        } else {
            ArticleList(
                articles = serachArticles,
                isRefreshing = serachArticles.loadState.refresh is LoadState.Loading,
                onArticleClick = onArticleClick,
                onRefresh = { serachArticles.refresh() },
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }
}