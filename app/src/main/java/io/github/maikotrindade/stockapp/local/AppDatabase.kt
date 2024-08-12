package io.github.maikotrindade.stockapp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.maikotrindade.stockapp.local.dao.StockDao
import io.github.maikotrindade.stockapp.local.entity.StockEntity

@Database(
    entities = [
        StockEntity::class,
    ],
    version = AppDatabase.DB_VERSION
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DB_VERSION: Int = 1
        const val DB_NAME = "AppDatabase"
    }
    abstract fun stockDao(): StockDao

}