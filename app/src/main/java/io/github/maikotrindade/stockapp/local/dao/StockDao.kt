package io.github.maikotrindade.stockapp.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.maikotrindade.stockapp.local.entity.StockEntity

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stocks: List<StockEntity>)

    @Query("SELECT * FROM stocks")
    fun getStocks(): List<StockEntity>

    @Query(
        """
    SELECT * FROM stocks 
    WHERE LOWER(ticker) LIKE LOWER(:query) || '%' OR LOWER(name) LIKE LOWER(:query) || '%'
    ORDER BY 
        CASE 
            WHEN LOWER(ticker) = LOWER(:query) THEN 1
            WHEN LOWER(name) = LOWER(:query) THEN 1
            ELSE 2
        END,
        name ASC
"""
    )
    fun getStocksByNameOrTicker(query: String): List<StockEntity>

}