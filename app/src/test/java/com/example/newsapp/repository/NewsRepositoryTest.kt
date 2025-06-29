package com.example.newsapp.repository

import com.example.newsapp.data.local.ArticleDao
import com.example.newsapp.data.local.ArticleEntity
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Source
import com.example.newsapp.data.remote.NewsApiService
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.utils.InternetMonitor
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class NewsRepositoryTest {

    private lateinit var repository: NewsRepository

    private val api: NewsApiService = Mockito.mock()
    private val dao: ArticleDao = Mockito.mock()
    private val monitor: InternetMonitor = Mockito.mock()

    @Before
    fun setup() {
        repository = NewsRepository(api, dao, monitor)
    }

    @Test
    fun `getSavedArticles returns list of articles`() = runTest {
        val sampleEntity = ArticleEntity(
            url = "https://example.com/breaking-news",
            sourceId = "bbc",
            sourceName = "BBC",
            author = "Jane Doe",
            title = "Breaking News",
            description = "Some news description",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2025-06-29T12:00:00Z",
            content = "Full news content here."
        )
        val flow = flowOf(listOf(sampleEntity))

        whenever(dao.getAll()).thenReturn(flow)

        val result = repository.getSavedArticles().first()

        TestCase.assertEquals("Breaking News", result[0].title)
        TestCase.assertEquals("Some news description", result[0].description)
    }

    @Test
    fun `saveArticle calls DAO insert`() = runTest {
        val sampleArticle = Article(
            source = Source(id = "techcrunch", name = "TechCrunch"),
            author = "John Doe",
            title = "Sample Title",
            description = "Sample description",
            url = "https://example.com/sample-article",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2025-06-29T14:00:00Z",
            content = "Sample content of the article"
        )

        repository.saveArticle(sampleArticle)

        verify(dao).upsertArticle(argThat { url == "https://example.com/sample-article" })
    }

    @Test
    fun `deleteArticle calls DAO delete`() = runTest {
        whenever(dao.deleteByUrl("url")).thenReturn(1)

        repository.deleteArticle("url")

        verify(dao).deleteByUrl("url")
    }
}