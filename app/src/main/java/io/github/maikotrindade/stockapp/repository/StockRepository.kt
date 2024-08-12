package io.github.maikotrindade.stockapp.repository

import io.github.maikotrindade.stockapp.common.ResultState
import io.github.maikotrindade.stockapp.remote.model.Stock
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getStocks(query: String, isRefresh: Boolean): Flow<ResultState<List<Stock>>>

}
