package io.github.maikotrindade.stockapp.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Stock(
    @SerializedName("ticker")
    var ticker: String,

    @SerializedName("name")
    var companyName: String,

    @SerializedName("currentPrice")
    var currentPrice: Double,

    ) : Serializable