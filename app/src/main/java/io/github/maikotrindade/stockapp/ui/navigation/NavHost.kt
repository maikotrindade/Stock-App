package io.github.maikotrindade.stockapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.maikotrindade.stockapp.remote.model.Stock
import io.github.maikotrindade.stockapp.ui.screen.detail.DetailScreen
import io.github.maikotrindade.stockapp.ui.screen.home.HomeScreen

private const val STOCK_ARG = "stock"

@Composable
fun StockAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationItem.Home.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = NavigationItem.Home.route) {
            NavigateToHome(navController)
        }

        composable(route = NavigationItem.Detail.route) {
            NavigateToDetail(navController)
        }
    }
}

@Composable
private fun NavigateToHome(navController: NavHostController) {
    HomeScreen(
        onSelectStock = {
            navController.currentBackStackEntry?.savedStateHandle?.set(STOCK_ARG, it)
            navController.navigate(NavigationState.Details.toString())
        }
    )
}

@Composable
private fun NavigateToDetail(navController: NavHostController) {
    val stock = navController.previousBackStackEntry?.savedStateHandle?.get<Stock>(STOCK_ARG)
    stock?.let {
        DetailScreen(navController = navController, it)
    }
}