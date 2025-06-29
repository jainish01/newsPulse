package com.example.newsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.local.ArticleEntity

class TestPagingSourceEntity : PagingSource<Int, ArticleEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleEntity> {
        val dummyEntity = ArticleEntity(
            url = "https://example.com",
            author = "Author",
            content = "Test content",
            description = "Description",
            publishedAt = "2025-06-29T12:00:00Z",
            sourceId = "source_id",
            sourceName = "Test Source",
            title = "Test Title",
            urlToImage = "https://example.com/image.png"
        )

        return LoadResult.Page(
            data = listOf(dummyEntity),
            prevKey = null,
            nextKey = null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleEntity>): Int? = null
}
