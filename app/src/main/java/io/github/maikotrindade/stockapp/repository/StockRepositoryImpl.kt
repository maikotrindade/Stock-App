package io.github.maikotrindade.stockapp.repository

import io.github.maikotrindade.stockapp.remote.common.ErrorState
import io.github.maikotrindade.stockapp.remote.common.ResultState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockDataSource: StockDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : StockRepository {

    override suspend fun getStocks(query: String, isRefresh: Boolean) = flow {
        emit(ResultState.loading())

        val stocksResult = if (!isRefresh && stockDataSource.hasCache()) {
            stockDataSource.getFromLocal(query)
        } else {
            stockDataSource.getFromRemote()
        }

        if (stocksResult.status == ResultState.Status.SUCCESS) {
            stocksResult.data?.let { list ->
                emit(ResultState.success(list))
            }
        } else if (stocksResult.status == ResultState.Status.ERROR) {
            emit(
                ResultState.error(
                    error = stocksResult.error ?: ErrorState.UNKNOWN_ERROR,
                    data = stocksResult.data
                )
            )
        }
    }.flowOn(ioDispatcher)
}