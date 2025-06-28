package com.example.newsapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {
    private val _homeArticles = MutableStateFlow<Resource<List<Article>>>(Resource.Idle)
    val homeArticles: StateFlow<Resource<List<Article>>> = _homeArticles

    private val _searchArticles = MutableStateFlow<Resource<List<Article>>>(Resource.Idle)
    val searchArticles: StateFlow<Resource<List<Article>>> = _searchArticles

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val savedArticles: StateFlow<List<Article>> = repository.getSavedArticles().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun saveArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveArticle(article)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        loadHeadlines(query)
    }

    fun removeArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArticle(article.url.toString())
        }
    }

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    init {
        loadHeadlines()
    }

    fun loadHeadlines(query: String? = null) {
        viewModelScope.launch {
            repository.getTopHeadlines(query).collect {
                if (query.isNullOrBlank()) {
                    _homeArticles.value = it
                } else {
                    _searchArticles.value = it
                }
            }
        }
    }
}