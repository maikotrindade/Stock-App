package io.github.maikotrindade.stockapp.remote.api

import io.github.maikotrindade.stockapp.remote.model.Stock
import retrofit2.http.GET

interface StockApi {

    @GET("0e1d4f8d517698cfdced49f5e59567be/raw/9158ad254e92aaffe215e950f4846a23a0680703/mock-stocks.json")
    suspend fun getStocks(): List<Stock>

}