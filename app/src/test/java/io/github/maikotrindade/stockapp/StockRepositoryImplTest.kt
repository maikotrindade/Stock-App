package io.github.maikotrindade.stockapp

import io.github.maikotrindade.stockapp.remote.common.ErrorState
import io.github.maikotrindade.stockapp.remote.common.ResultState
import io.github.maikotrindade.stockapp.remote.model.Stock
import io.github.maikotrindade.stockapp.repository.StockDataSource
import io.github.maikotrindade.stockapp.repository.StockRepositoryImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StockRepositoryImplTest {

    private val stockDataSource: StockDataSource = mockk()
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Unconfined

    private lateinit var stockRepository: StockRepositoryImpl

    private val mockedStocks = listOf(
        Stock("ULUG", "Vanguard Resources", 485.02),
        Stock("GZRQ", "Omni Industries", 448.50)
    )

    @Before
    fun setUp() {
        stockRepository = StockRepositoryImpl(stockDataSource, ioDispatcher)
    }

    @Test
    fun getStocksEmitsLoadingThenSuccessWhenHasCache() = runTest {
        every { stockDataSource.hasCache() } returns true
        every { stockDataSource.getFromLocal(any()) } returns ResultState.success(mockedStocks)

        val result = stockRepository.getStocks("query", false)
        assertEquals(ResultState.Status.LOADING, result.first().status)

        assertEquals(ResultState.success(mockedStocks), result.last())
    }

    @Test
    fun getStocksEmitsLoadingThenErrorRemoteFetchFails() = runTest {
        every { stockDataSource.hasCache() } returns false
        coEvery { stockDataSource.getFromRemote() } returns ResultState.error(ErrorState.UNKNOWN_ERROR)

        val result = stockRepository.getStocks("query", true)
        assertEquals(ResultState.Status.LOADING, result.first().status)

        assertEquals(ErrorState.UNKNOWN_ERROR, result.last().error)
    }


    @Test
    fun getStocksEmitsLoadingThenSuccessRemoteFetchSucceeds() = runTest {
        every { stockDataSource.hasCache() } returns false
        coEvery { stockDataSource.getFromRemote() } returns ResultState.success(mockedStocks)

        val result = stockRepository.getStocks("query", true)
        assertEquals(ResultState.Status.LOADING, result.first().status)

        assertEquals(ResultState.success(mockedStocks), result.last())
    }

    @Test
    fun getStocksEmitsErrorNoCacheNoRemote() = runTest {
        every { stockDataSource.hasCache() } returns false
        coEvery { stockDataSource.getFromRemote() } returns ResultState.error(ErrorState.UNKNOWN_ERROR)

        val result = stockRepository.getStocks("query", true)
        assertEquals(ResultState.Status.LOADING, result.first().status)

        assertEquals(ErrorState.UNKNOWN_ERROR, result.last().error)
    }
}