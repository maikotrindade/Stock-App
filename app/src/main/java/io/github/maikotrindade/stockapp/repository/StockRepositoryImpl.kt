package io.github.maikotrindade.stockapp.repository

import io.github.maikotrindade.stockapp.remote.common.ErrorState
import io.github.maikotrindade.stockapp.remote.common.ResultState
import io.github.maikotrindade.stockapp.remote.model.Stock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockDataSource: StockDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : StockRepository {

    override suspend fun getStocks(
        query: String,
        isRefresh: Boolean
    ): Flow<ResultState<List<Stock>>> {
        return flow {
            emit(ResultState.loading())
            val result = stockDataSource.getStocks(query, isRefresh)
            if (result.status == ResultState.Status.SUCCESS) {
                result.data?.let { list ->
                    emit(ResultState.success(list))
                }
            } else if (result.status == ResultState.Status.ERROR) {
                emit(
                    ResultState.error(
                        error = result.error ?: ErrorState.UNKNOWN_ERROR,
                        data = result.data
                    )
                )
            }
        }.flowOn(ioDispatcher)
    }

}