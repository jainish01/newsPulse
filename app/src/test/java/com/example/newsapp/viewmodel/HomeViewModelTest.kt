package com.example.newsapp.viewmodel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Source
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.paging.TestPagingSource
import com.example.newsapp.paging.TestPagingSourceEntity
import com.example.newsapp.ui.HomeViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val repository: NewsRepository = Mockito.mock()
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        whenever(repository.getSavedArticles()).thenReturn(flowOf(emptyList()))
        whenever(repository.getSavedArticlesPager()).thenReturn(
            Pager(
                config = PagingConfig(20),
                pagingSourceFactory = { TestPagingSourceEntity() }
            )
        )
        whenever(repository.getHomePager()).thenReturn(
            Pager(
                config = PagingConfig(20),
                pagingSourceFactory = { TestPagingSource() }
            )
        )

        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `selectArticle sets selectedArticle state`() = runTest {
        val article = Article(
            source = Source("1", "Test Source"),
            author = "Author",
            title = "Title",
            description = "Description",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2025-06-29",
            content = "Sample content"
        )

        viewModel.selectArticle(article)
        assertEquals(article, viewModel.selectedArticle.value)
    }

    @Test
    fun `updateSearchQuery sets query state`() = runTest {
        viewModel.updateSearchQuery("bitcoin")
        assertEquals("bitcoin", viewModel.searchQuery.value)
    }

    @Test
    fun `saveArticle calls repository`() = runTest {
        val article = Article(
            source = Source("1", "Test Source"),
            author = "Author",
            title = "Title",
            description = "Description",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2025-06-29",
            content = "Sample content"
        )

        viewModel.saveArticle(article)
        advanceUntilIdle()

        verify(repository).saveArticle(article)
    }

    @Test
    fun `removeArticle calls repository`() = runTest {
        val article = Article(
            source = Source("1", "Test Source"),
            author = "Author",
            title = "Title",
            description = "Description",
            url = "https://example.com",
            urlToImage = "https://example.com/image.jpg",
            publishedAt = "2025-06-29",
            content = "Sample content"
        )

        whenever(repository.deleteArticle("https://example.com")).thenReturn(1)

        viewModel.removeArticle(article)
        advanceUntilIdle()

        verify(repository).deleteArticle("https://example.com")
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}