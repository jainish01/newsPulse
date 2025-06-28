package com.example.newsapp.data.mapper

import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Source
import com.example.newsapp.data.remote.model.ArticleDto
import com.example.newsapp.data.remote.model.SourceDto

fun ArticleDto.toModel(): Article {
    return Article(
        source = this.source?.toModel(),
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}

fun SourceDto.toModel(): Source {
    return Source(
        id = this.id,
        name = this.name
    )
}