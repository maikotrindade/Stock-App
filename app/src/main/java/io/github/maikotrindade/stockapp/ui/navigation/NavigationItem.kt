package io.github.maikotrindade.stockapp.ui.navigation

sealed class NavigationState {
    data object Home : NavigationState()
    data object Details : NavigationState()
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(NavigationState.Home.toString())
    data object Detail : NavigationItem(NavigationState.Details.toString())
}