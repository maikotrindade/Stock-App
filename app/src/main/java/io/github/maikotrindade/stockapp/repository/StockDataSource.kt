package io.github.maikotrindade.stockapp.repository

import io.github.maikotrindade.stockapp.remote.common.ErrorState
import io.github.maikotrindade.stockapp.remote.common.ResultState
import io.github.maikotrindade.stockapp.local.dao.StockDao
import io.github.maikotrindade.stockapp.mapping.StockMapper
import io.github.maikotrindade.stockapp.remote.api.StockApi
import io.github.maikotrindade.stockapp.remote.model.Stock
import javax.inject.Inject

class StockDataSource @Inject constructor(
    private val stockApi: StockApi,
    private val stockDao: StockDao
) {
    suspend fun getStocks(query: String, isRefresh: Boolean): ResultState<List<Stock>> {
        val hasCache = stockDao.getStocks().isNotEmpty()
        return try {
            if (hasCache && !isRefresh) {
                val localResult = if (query.isNotEmpty()) {
                    stockDao.getStocksByNameOrTicker(query)
                } else {
                    stockDao.getStocks()
                }
                ResultState.success(
                    localResult.map { stockEntity -> StockMapper.fromLocal(stockEntity) }
                )
            } else {
                val apiResponse = stockApi.getStocks()
                apiResponse.let { stocksFromRemote ->
                    val stocks = stocksFromRemote.map { StockMapper.fromRemote(it) }
                    stockDao.insertAll(stocks)
                }
                ResultState.success(apiResponse)
            }
        } catch (e: Exception) {
            ResultState.error(ErrorState(message = e.localizedMessage))
        }
    }
}