package com.bulat.newsaggregator.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {
    @Query("SELECT * FROM news ORDER BY date DESC")
    suspend fun getAllNews(): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>)

    @Query("DELETE FROM news")
    suspend fun clearAll()
} 