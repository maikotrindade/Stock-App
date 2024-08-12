package io.github.maikotrindade.stockapp.ui.navigation

enum class NavigationState {
    HOME,
    DETAILS,
}

sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(NavigationState.HOME.name)
    data object Details : NavigationItem(NavigationState.DETAILS.name)
}