package com.example.newsapp.data.remote

import com.example.newsapp.data.remote.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("q") query: String? = null,
        @Query("apiKey") apiKey: String
    ): NewsResponse
}