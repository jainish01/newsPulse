package com.example.newsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao{

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    fun upsertArticle(article: ArticleEntity)

    @Query("SELECT * FROM articles")
    fun getAll() : Flow<List<ArticleEntity>>

    @Query("DELETE FROM articles WHERE url = :url")
    fun deleteByUrl(url: String): Int

}