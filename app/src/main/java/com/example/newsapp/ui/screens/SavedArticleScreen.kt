package com.example.newsapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsapp.data.model.Article
import com.example.newsapp.ui.views.ArticleList
import com.example.newsapp.ui.views.ArticlesView

@Composable
fun SavedArticleScreen(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    onDeleteArticle: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        ArticlesView (
            articles,
            onArticleClick = onArticleClick,
            swipeToDelete = true,
            onDelete = onDeleteArticle,
        )
    }
}