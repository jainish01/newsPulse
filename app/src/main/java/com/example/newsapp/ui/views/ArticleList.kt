package com.example.newsapp.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.newsapp.data.model.Article

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ArticleList(
    articles: LazyPagingItems<Article>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onArticleClick: (Article) -> Unit,
    swipeToDelete: Boolean = false,
    onDelete: (Article) -> Unit = {},
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(articles.itemCount) { index ->
                val article = articles[index]
                article?.let {
                    if (swipeToDelete) {
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart) {
                                    onDelete(article)
                                    true
                                } else false
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(
                                DismissDirection.StartToEnd,
                                DismissDirection.EndToStart
                            ),
                            background = {}, // no persistent background
                            dismissContent = {
                                ArticleItem(article = article, onClick = { onArticleClick(article) })
                            }
                        )
                    } else {
                        ArticleItem(article = article, onClick = { onArticleClick(article) })
                    }
                }
            }

            when (articles.loadState.append) {
                is LoadState.Loading -> item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
                is LoadState.Error -> item {
                    Text(
                        text = "Error loading more items",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                else -> {}
            }

            if (articles.loadState.refresh is LoadState.NotLoading && articles.itemCount == 0) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No articles found")
                    }
                }
            }

            if (articles.loadState.refresh is LoadState.Error) {
                item {
                    val error = articles.loadState.refresh as LoadState.Error
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${error.error.localizedMessage ?: "Unknown error"}",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
