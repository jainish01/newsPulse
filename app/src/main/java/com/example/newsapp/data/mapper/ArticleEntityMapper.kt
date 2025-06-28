package com.example.newsapp.data.mapper

import com.example.newsapp.data.local.ArticleEntity
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Source

fun Article.toEntity(): ArticleEntity {
    return ArticleEntity(
        url = this.url ?: "",
        sourceId = this.source?.id,
        sourceName = this.source?.name,
        author = this.author,
        title = this.title,
        description = this.description,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}

fun ArticleEntity.toModel(): Article {
    return Article(
        source = Source(id = this.sourceId, name = this.sourceName),
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}