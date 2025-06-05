package com.bulat.newsaggregator.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bulat.newsaggregator.core.domain.repository.NewsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class NewsSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: NewsRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            repository.getNews().first()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
} 