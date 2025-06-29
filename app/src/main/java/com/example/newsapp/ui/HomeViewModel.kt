package com.example.newsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.newsapp.data.mapper.toModel
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val savedArticlesPagingFlow: Flow<PagingData<Article>> =
        repository.getSavedArticlesPager()
            .flow
            .map { pagingData -> pagingData.map { it.toModel() } }
            .cachedIn(viewModelScope)

    val savedArticles: StateFlow<List<Article>> = repository.getSavedArticles().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val homePagingFlow: Flow<PagingData<Article>> = repository
        .getHomePager()
        .flow
        .cachedIn(viewModelScope)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchPagingFlow: Flow<PagingData<Article>> = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                repository.getSearchPager(query).flow
            }
        }
        .cachedIn(viewModelScope)

    fun saveArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveArticle(article)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun removeArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArticle(article.url.toString())
        }
    }

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }
}