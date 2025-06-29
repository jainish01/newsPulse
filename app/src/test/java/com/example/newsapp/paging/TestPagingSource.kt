package com.example.newsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Source

class TestPagingSource : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return LoadResult.Page(
            data = listOf(
                Article(
                    source = Source("1", "Test Source"),
                    author = "Author",
                    title = "Title",
                    description = "Description",
                    url = "https://example.com",
                    urlToImage = "https://example.com/image.jpg",
                    publishedAt = "2025-06-29",
                    content = "Sample content"
                )
            ),
            prevKey = null,
            nextKey = null
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = null
}
