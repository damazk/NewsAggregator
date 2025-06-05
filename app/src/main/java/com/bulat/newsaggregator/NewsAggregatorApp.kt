package com.bulat.newsaggregator

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.bulat.newsaggregator.core.worker.NewsSyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class NewsAggregatorApp : Application() {
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        val workRequest = PeriodicWorkRequestBuilder<NewsSyncWorker>(30, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "news_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
} 