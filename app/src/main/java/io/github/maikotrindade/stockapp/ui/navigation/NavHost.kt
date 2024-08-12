package io.github.maikotrindade.stockapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        composable(
            route = NavigationItem.Home.route
        ) {
            HomeScreen(
                onSelectStock = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(STOCK_ARG, it)
                    navController.navigate(NavigationState.DETAILS.name)
                }
            )
        }
    }
}