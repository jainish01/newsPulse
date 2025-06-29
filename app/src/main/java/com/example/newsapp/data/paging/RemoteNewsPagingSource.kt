package com.example.newsapp.data.paging

import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.mapper.toModel
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.remote.NewsApiService
import com.example.newsapp.utils.InternetMonitor

class RemoteNewsPagingSource(
    private val api: NewsApiService,
    private val query: String?,
    private val apiKey: String,
    private val internetMonitor: InternetMonitor
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        if (internetMonitor.isAvailable){
            return try {
                val page = params.key ?: 1
                val response = api.getTopHeadlines(
                    page = page,
                    pageSize = params.loadSize,
                    query = query,
                    apiKey = apiKey
                )
                val articles = response.articles.map { it.toModel() }

                LoadResult.Page(
                    data = articles,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (articles.isEmpty()) null else page + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(Exception("Something went wrong"))
            }
        } else {
            return LoadResult.Error(Exception("No internet connection"))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
