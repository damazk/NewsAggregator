package com.bulat.newsaggregator.di

import android.content.Context
import androidx.room.Room
import com.bulat.newsaggregator.data.local.AppDatabase
import com.bulat.newsaggregator.data.local.NewsDao
import com.bulat.newsaggregator.data.remote.NewsApi
import com.bulat.newsaggregator.data.repository.NewsRepositoryImpl
import com.bulat.newsaggregator.domain.repository.NewsRepository
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModuleProvider {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val apiKey = "test"

        val apiKeyInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val requestWithApiKey = chain.request().newBuilder()
                    .addHeader("api-key", apiKey)
                    .build()
                return chain.proceed(requestWithApiKey)
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {

        val gson = GsonBuilder().setLenient().create()
        val baseUrl = "https://content.guardianapis.com/"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "news_db").build()

    @Provides
    @Singleton
    fun provideNewsDao(db: AppDatabase): NewsDao = db.newsDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinder {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(repo: NewsRepositoryImpl): NewsRepository
}