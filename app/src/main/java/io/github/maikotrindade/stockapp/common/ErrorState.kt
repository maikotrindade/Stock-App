package io.github.maikotrindade.stockapp.common

import androidx.annotation.StringRes

data class ErrorState(
    val message: String? = null,
    val code: Int = 0,
    @StringRes val errorMessage: Int? = null
) {

    companion object {
        val UNKNOWN_ERROR = ErrorState(null, 0)
    }
}