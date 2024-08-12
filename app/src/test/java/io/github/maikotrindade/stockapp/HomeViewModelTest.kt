package io.github.maikotrindade.stockapp

import io.github.maikotrindade.stockapp.remote.common.ErrorState
import io.github.maikotrindade.stockapp.remote.common.ResultState
import io.github.maikotrindade.stockapp.remote.model.Stock
import io.github.maikotrindade.stockapp.repository.StockRepository
import io.github.maikotrindade.stockapp.ui.screen.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var repository: StockRepository
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    private val mockedStocks = listOf(
        Stock("ULUG", "Vanguard Resources", 485.02),
        Stock("GZRQ", "Omni Industries", 448.50)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun getStocksShouldReturnSuccess() = runTest {
        val successResult = ResultState.success(mockedStocks)
        coEvery { repository.getStocks(any(), true) } returns flowOf(successResult)

        viewModel.getStocks(true)
        advanceUntilIdle()

        assertEquals(ResultState.success(mockedStocks), viewModel.stocksFlow.value)
    }

    @Test
    fun getStocksShouldReturnError() = runTest {
        val errorResultState = ResultState.error<List<Stock>>(ErrorState("Network Error", 0))
        coEvery { repository.getStocks(any(), true) } throws Exception("Network Error")

        viewModel.getStocks(true)
        advanceUntilIdle()

        assertEquals(errorResultState, viewModel.stocksFlow.value)
    }
}
