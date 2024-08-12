package io.github.maikotrindade.stockapp.repository

import io.github.maikotrindade.stockapp.local.dao.StockDao
import io.github.maikotrindade.stockapp.mapping.StockMapper
import io.github.maikotrindade.stockapp.remote.api.StockApi
import io.github.maikotrindade.stockapp.remote.common.ErrorState
import io.github.maikotrindade.stockapp.remote.common.ResultState
import io.github.maikotrindade.stockapp.remote.model.Stock
import javax.inject.Inject

class StockDataSource @Inject constructor(
    private val stockApi: StockApi,
    private val stockDao: StockDao
) {
    fun hasCache() = stockDao.getStocks().isNotEmpty()

    fun getFromLocal(query: String) : ResultState<List<Stock>> = try {
        val stocks = if (query.isNotEmpty()) {
            stockDao.getStocksByNameOrTicker(query)
        } else {
            stockDao.getStocks()
        }
        ResultState.success(
            stocks.map { stockEntity -> StockMapper.fromLocal(stockEntity) }
        )
    } catch (e: Exception) {
        ResultState.error(ErrorState(message = e.localizedMessage))
    }

    suspend fun getFromRemote() = try {
        val apiResponse = stockApi.getStocks()
        apiResponse.let { stocksFromRemote ->
            val stocks = stocksFromRemote.map { StockMapper.fromRemote(it) }
            stockDao.insertAll(stocks)
        }
        ResultState.success(apiResponse)
    } catch (e: Exception) {
        ResultState.error(ErrorState(message = e.localizedMessage))
    }
}