package io.github.maikotrindade.stockapp.common

data class ResultState<out T>(
    val status: Status,
    val data: T?,
    val error: ErrorState? = null,
    val message: String? = null
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): ResultState<T> {
            return ResultState(Status.SUCCESS, data, null, null)
        }

        fun <T> error(error: ErrorState, message: String? = null, data: T? = null): ResultState<T> {
            return ResultState(Status.ERROR, data, error, message)
        }

        fun <T> loading(data: T? = null): ResultState<T> {
            return ResultState(Status.LOADING, data, null, null)
        }
    }
}