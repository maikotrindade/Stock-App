package io.github.maikotrindade.stockapp.mapping

import io.github.maikotrindade.stockapp.local.entity.StockEntity
import io.github.maikotrindade.stockapp.remote.model.Stock

object StockMapper : Mapper<StockEntity, Stock> {
    override fun fromRemote(stock: Stock): StockEntity {
        return StockEntity(
            ticker = stock.ticker,
            name = stock.companyName,
            currentPrice = stock.currentPrice,
        )
    }

    override fun fromLocal(stock: StockEntity): Stock {
        return Stock(
            ticker = stock.ticker,
            companyName = stock.name,
            currentPrice = stock.currentPrice,
        )
    }
}