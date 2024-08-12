package io.github.maikotrindade.stockapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.maikotrindade.stockapp.BuildConfig
import io.github.maikotrindade.stockapp.remote.api.StockApi
import io.github.maikotrindade.stockapp.remote.network.AddHeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private const val REQUEST_TIMEOUT_S = 30L

    @Singleton
    @Provides
    fun provideBaseRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideOkClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIMEOUT_S, TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT_S, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(0, 3, TimeUnit.MINUTES))
            .protocols(listOf(Protocol.HTTP_1_1))
            .addNetworkInterceptor(AddHeaderInterceptor())
            .build()

    @Singleton
    @Provides
    fun provideUserRetrofitService(retrofit: Retrofit): StockApi {
        return retrofit.create(StockApi::class.java)
    }
}