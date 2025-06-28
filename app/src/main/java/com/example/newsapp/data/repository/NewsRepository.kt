package com.example.newsapp.data.repository

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.local.ArticleDao
import com.example.newsapp.data.mapper.toEntity
import com.example.newsapp.data.mapper.toModel
import com.example.newsapp.data.remote.NewsApiService
import com.example.newsapp.utils.InternetMonitor
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsApiService,
    private val dao: ArticleDao,
    private val internetMonitor: InternetMonitor
) {
    fun getTopHeadlines(query: String? = null): Flow<Resource<List<Article>>> = flow {
        if (internetMonitor.isAvailable){
            try {
                emit(Resource.Loading)
                val response = api.getTopHeadlines(query = query, apiKey = BuildConfig.NEWS_API_KEY)
                val articles = response.articles.map { it.toModel() }
                emit(Resource.Success(articles))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
            }
        } else {
            emit(Resource.Error("No internet connection"))
        }
    }


    suspend fun saveArticle(article: Article) = dao.upsertArticle(article.toEntity())

    fun getSavedArticles(): Flow<List<Article>> = dao.getAll().map{ list -> list.map { articleEntity -> articleEntity.toModel() } }

    suspend fun deleteArticle(url: String) = dao.deleteByUrl(url)
}