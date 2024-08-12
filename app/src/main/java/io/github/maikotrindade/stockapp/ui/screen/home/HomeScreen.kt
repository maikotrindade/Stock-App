package io.github.maikotrindade.stockapp.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.maikotrindade.stockapp.R
import io.github.maikotrindade.stockapp.common.Formatter.formatCurrency
import io.github.maikotrindade.stockapp.remote.common.ResultState
import io.github.maikotrindade.stockapp.remote.model.Stock
import io.github.maikotrindade.stockapp.ui.components.LoadingContent

@Composable
fun HomeScreen(
    onSelectStock: (Stock) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val stocksState = viewModel.stocksFlow.collectAsState()
    val searchQueryState = viewModel.searchQueryFlow.collectAsState()
    HomeScreenContent(
        stocksResult = stocksState.value,
        searchQuery = searchQueryState.value,
        onSearchStock = viewModel::searchStocks,
        onRefreshStock = viewModel::getStocks,
        onSelectedStock = onSelectStock
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    stocksResult: ResultState<List<Stock>>?,
    searchQuery: String,
    onSearchStock: (String) -> Unit,
    onRefreshStock: (Boolean) -> Unit,
    onSelectedStock: (Stock) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                title = {
                    SearchTile(searchQuery, onSearchStock)
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.padding(16.dp),
                        imageVector = Icons.Sharp.Home,
                        contentDescription = "home",
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues)) {
            when (stocksResult?.status) {
                ResultState.Status.SUCCESS -> StocksContent(
                    stocks = stocksResult.data.orEmpty(),
                    searchQuery = searchQuery,
                    onSelectedStock = onSelectedStock
                )

                ResultState.Status.ERROR -> ErrorStateContent(onRefreshStock)
                else -> LoadingContent()
            }
        }
    }
}

@Composable
private fun SearchTile(searchQuery: String, onSearchStock: (String) -> Unit) {
    var isSearchMode by remember { mutableStateOf(false) }

    if (isSearchMode) {
        TextField(
            value = searchQuery,
            onValueChange = { onSearchStock(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(stringResource(R.string.home_screen_search_placeholder))
            },
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            isSearchMode = false
                        },
                    imageVector = Icons.Sharp.Close,
                    contentDescription = "exit search",
                )
            },
            singleLine = true,
        )
    } else {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        isSearchMode = true
                    },
                imageVector = Icons.Sharp.Search,
                contentDescription = "search",
            )
        }
    }
}

@Composable
private fun StocksContent(
    stocks: List<Stock>,
    searchQuery: String,
    onSelectedStock: (Stock) -> Unit
) {
    if (stocks.isNotEmpty()) {
        LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
            items(items = stocks) { stock ->
                StockTile(stock, onSelectedStock)
            }
        }
    } else {
        EmptyStateContent(searchQuery)
    }
}

@Composable
private fun ErrorStateContent(onRefreshStock: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.t_rex_error_offline),
            contentDescription = "Something went wrong"
        )
        Text(
            text = stringResource(R.string.home_screen_generic_error),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.home_screen_error_try_again),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Button(onClick = {
            onRefreshStock(true)
        }
        ) {
            Text(
                text = stringResource(R.string.home_screen_error_try_again_button),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
private fun EmptyStateContent(searchQuery: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = "Something went wrong"
        )
        Text(
            text = stringResource(R.string.home_screen_no_results, searchQuery),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.home_screen_no_results_message),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun StockTile(stock: Stock, onSelectedStock: (Stock) -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        val formattedPrice = remember(stock.currentPrice) {
            stock.currentPrice.formatCurrency()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        onSelectedStock(stock)
                    }
                )
                .background(Color.White)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stock.ticker,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stock.companyName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}
