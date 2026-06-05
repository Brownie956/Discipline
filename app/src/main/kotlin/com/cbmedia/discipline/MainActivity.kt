package com.cbmedia.discipline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cbmedia.discipline.ui.Screen
import com.cbmedia.discipline.ui.config.ConfigScreen
import com.cbmedia.discipline.ui.config.ConfigViewModel
import com.cbmedia.discipline.ui.config.ConfigViewModelFactory
import com.cbmedia.discipline.ui.game.GameScreen
import com.cbmedia.discipline.ui.game.GameViewModel
import com.cbmedia.discipline.ui.game.GameViewModelFactory
import com.cbmedia.discipline.ui.home.HomeScreen
import com.cbmedia.discipline.ui.home.HomeViewModel
import com.cbmedia.discipline.ui.home.HomeViewModelFactory
import com.cbmedia.discipline.ui.theme.DisciplineTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DisciplineTheme {
                val app = application as DisciplineApplication
                val repository = app.gameRepository
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) {
                        val viewModel: HomeViewModel = viewModel(
                            factory = HomeViewModelFactory(repository)
                        )

                        val games by viewModel.gameSummaries.collectAsStateWithLifecycle()

                        HomeScreen(
                            games = games,
                            onGameClick = { gameId ->
                                navController.navigate(Screen.Game.createRoute(gameId))
                            },
                            onCreateNewGameClick = {
                                navController.navigate(Screen.Config.route)
                            }
                        )
                    }

                    composable(Screen.Config.route) {
                        val viewModel: ConfigViewModel = viewModel(
                            factory = ConfigViewModelFactory(repository)
                        )

                        ConfigScreen(
                            gameName = viewModel.gameName,
                            onGameNameChanged = viewModel::updateGameName,
                            baseDays = viewModel.baseDays,
                            onBaseDaysChanged = viewModel::updateBaseDays,
                            useCardCountAsBaseDays = viewModel.useCardCountAsBaseDays,
                            onUseCardCountAsBaseDaysChanged = viewModel::updateUseCardCountAsBaseDays,
                            totalSelectedCards = viewModel.totalSelectedCards,
                            cardCounts = viewModel.cardCounts,
                            onIncrement = viewModel::increment,
                            onDecrement = viewModel::decrement,
                            onStartGame = {
                                viewModel.createGame { gameId ->
                                    navController.navigate(Screen.Game.createRoute(gameId)) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            }
                        )
                    }

                    composable(
                        route = Screen.Game.route,
                        arguments = listOf(
                            navArgument("gameId") {
                                type = NavType.LongType
                            }
                        )
                    ) { backStackEntry ->

                        val gameId = backStackEntry.arguments?.getLong("gameId")
                            ?: return@composable

                        val viewModel: GameViewModel = viewModel(
                            factory = GameViewModelFactory(repository)
                        )

                        LaunchedEffect(gameId) {
                            viewModel.loadGame(gameId)
                        }

                        val game by viewModel.game.collectAsStateWithLifecycle()

                        game?.let { currentGame ->
                            GameScreen(
                                game = currentGame,
                                onDrawCard = viewModel::drawCard,
                                onEndGameEarly = {
                                    viewModel.endGameEarly()
                                    navController.popBackStack(Screen.Home.route, false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
