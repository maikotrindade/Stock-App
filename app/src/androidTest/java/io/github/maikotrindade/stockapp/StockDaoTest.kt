package io.github.maikotrindade.stockapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.maikotrindade.stockapp.local.AppDatabase
import io.github.maikotrindade.stockapp.local.dao.StockDao
import io.github.maikotrindade.stockapp.local.entity.StockEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StockDaoTest {
    private lateinit var stockDao: StockDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        stockDao = appDatabase.stockDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun shouldInsertAndGetStocks() {
        val stocks = listOf(
            StockEntity("ULUG", "Vanguard Resources", 485.02),
            StockEntity("GZRQ", "Omni Industries", 448.50)
        )
        stockDao.insertAll(stocks)

        val result = stockDao.getStocks()
        assertEquals(stocks.size, result.size)
        assertTrue(result.containsAll(stocks))
    }

    @Test
    fun shouldGetStocksByNameOrTicker() {
        val stocks = listOf(
            StockEntity("ULUG", "Vanguard Resources", 485.02),
            StockEntity("GZRQ", "Omni Industries", 448.50)
        )
        stockDao.insertAll(stocks)

        val resultByTicker = stockDao.getStocksByNameOrTicker("ULUG")
        assertEquals(1, resultByTicker.size)
        assertEquals("ULUG", resultByTicker[0].ticker)

        val resultByName = stockDao.getStocksByNameOrTicker("Omni Industries")
        assertEquals(1, resultByName.size)
        assertEquals("GZRQ", resultByName[0].ticker)

        val resultByPartialName = stockDao.getStocksByNameOrTicker("Vanguard")
        assertEquals(1, resultByPartialName.size)
        assertEquals("ULUG", resultByPartialName[0].ticker)
    }
}