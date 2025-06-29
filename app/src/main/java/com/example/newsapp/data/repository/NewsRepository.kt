package com.example.newsapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.newsapp.BuildConfig
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.local.ArticleDao
import com.example.newsapp.data.local.ArticleEntity
import com.example.newsapp.data.mapper.toEntity
import com.example.newsapp.data.mapper.toModel
import com.example.newsapp.data.paging.RemoteNewsPagingSource
import com.example.newsapp.data.remote.NewsApiService
import com.example.newsapp.utils.InternetMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsApiService,
    private val dao: ArticleDao,
    private val internetMonitor: InternetMonitor
) {

    fun getHomePager(): Pager<Int, Article> = Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 20),
        pagingSourceFactory = {
            RemoteNewsPagingSource(
                api = api,
                query = null,
                apiKey = BuildConfig.NEWS_API_KEY,
                internetMonitor
            )
        }
    )

    fun getSearchPager(query: String): Pager<Int, Article> = Pager(
        config = PagingConfig(pageSize = 20, initialLoadSize = 20),
        pagingSourceFactory = {
            RemoteNewsPagingSource(
                api = api,
                query = query,
                apiKey = BuildConfig.NEWS_API_KEY,
                internetMonitor
            )
        }
    )


    suspend fun saveArticle(article: Article) = dao.upsertArticle(article.toEntity())

    fun getSavedArticlesPager(): Pager<Int, ArticleEntity> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { dao.pagingSource() }
    )

    fun getSavedArticles(): Flow<List<Article>> = dao.getAll().map{ list -> list.map { articleEntity -> articleEntity.toModel() } }

    suspend fun deleteArticle(url: String) = dao.deleteByUrl(url)
}