package io.github.maikotrindade.stockapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.maikotrindade.stockapp.remote.common.ErrorState
import io.github.maikotrindade.stockapp.remote.common.ResultState
import io.github.maikotrindade.stockapp.remote.model.Stock
import io.github.maikotrindade.stockapp.repository.StockRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: StockRepository) : ViewModel() {

    private val _stocksFlow = MutableStateFlow<ResultState<List<Stock>>?>(null)
    val stocksFlow: StateFlow<ResultState<List<Stock>>?> = _stocksFlow

    private val _searchQueryFlow = MutableStateFlow("")
    val searchQueryFlow: StateFlow<String> = _searchQueryFlow

    init {
        getStocks(isRefresh = true)
    }

    @OptIn(FlowPreview::class)
    fun searchStocks(searchQuery: String) {
        _searchQueryFlow.value = searchQuery
        viewModelScope.launch {
            searchQueryFlow
                .debounce(DEBOUNCE_SEARCH)
                .distinctUntilChanged()
                .collect { _ ->
                    getStocks(isRefresh = false)
                }
        }
    }

    fun getStocks(isRefresh: Boolean) = viewModelScope.launch {
        repository.getStocks(_searchQueryFlow.value, isRefresh)
            .onStart { _stocksFlow.value = ResultState.loading() }
            .catch {
                _stocksFlow.value = ResultState.error(ErrorState(message = it.localizedMessage))
            }.collect { result ->
                result.let {
                    if (it.status == ResultState.Status.SUCCESS) {
                        it.data?.let { session ->
                            _stocksFlow.value = ResultState.success(session)
                        }
                    } else if (it.status == ResultState.Status.ERROR) {
                        _stocksFlow.value =
                            ResultState.error(error = it.error ?: ErrorState.UNKNOWN_ERROR)
                    }
                }
            }
    }

    companion object {
        val DEBOUNCE_SEARCH = 300.milliseconds
    }
}
