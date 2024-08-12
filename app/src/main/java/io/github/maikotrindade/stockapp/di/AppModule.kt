package io.github.maikotrindade.stockapp.di

import android.content.Context
import android.content.res.Resources
import io.github.maikotrindade.stockapp.repository.StockDataSource
import io.github.maikotrindade.stockapp.repository.StockRepository
import io.github.maikotrindade.stockapp.repository.StockRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataRepository(
        stockDataSource: StockDataSource,
        ioDispatcher: CoroutineDispatcher
    ): StockRepository = StockRepositoryImpl(stockDataSource, ioDispatcher)

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideResources(context: Context): Resources = context.resources
}