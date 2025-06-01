package com.bulat.newsaggregator.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bulat.newsaggregator.domain.model.NewsItem
import com.bulat.newsaggregator.domain.usecase.GetNewsUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val getNewsUseCase: GetNewsUseCase = mockk()
    private lateinit var viewModel: NewsViewModel

    val news = listOf(
        NewsItem(
            title = "title",
            description = "description",
            imageUrl = null,
            link = "l",
            date = "2024-01-01",
            author = "a",
            tags = listOf("tag")
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getNewsUseCase.invoke() } returns flowOf(news)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `news is updated from usecase`() = runTest {
        viewModel = NewsViewModel(getNewsUseCase)
        advanceUntilIdle()
        assertEquals(news[0], viewModel.uiState.news.firstOrNull())
        assertEquals(false, viewModel.uiState.isLoading)
        assertEquals(null, viewModel.uiState.error)
    }
} 