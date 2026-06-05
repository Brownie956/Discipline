package com.cbmedia.discipline.ui

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Config : Screen("config")
    data object Game : Screen("game/{gameId}") {
        fun createRoute(gameId: Long) = "game/$gameId"
    }
}
