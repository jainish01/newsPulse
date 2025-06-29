package com.example.newsapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticle(article: ArticleEntity)

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun pagingSource(): PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM articles")
    fun getAll() : Flow<List<ArticleEntity>>

    @Query("DELETE FROM articles WHERE url = :url")
    suspend fun deleteByUrl(url: String): Int
}
