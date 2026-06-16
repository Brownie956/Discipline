package com.cbmedia.discipline.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.GameSummary
import com.cbmedia.discipline.ui.components.EmptyGamesSummaryCard
import com.cbmedia.discipline.ui.components.GameSummaryCard
import java.time.LocalDate
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@Composable
fun HomeScreen(
    activeGames: List<GameSummary>,
    completedGames: List<GameSummary>,
    abandonedGames: List<GameSummary>,
    onGameClick: (Long) -> Unit,
    onCreateNewGameClick: () -> Unit,
    onDeleteGameClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var gamePendingDelete by remember {
        mutableStateOf<GameSummary?>(null)
    }

    gamePendingDelete?.let { game ->
        AlertDialog(
            onDismissRequest = {
                gamePendingDelete = null
            },
            title = {
                Text("Delete game?")
            },
            text = {
                Text("Are you sure you want to delete \"${game.name}\"? This cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteGameClick(game.id)
                        gamePendingDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        gamePendingDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNewGameClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create new game"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Active Games",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            if (activeGames.isEmpty()) {
                item {
                    EmptyGamesSummaryCard()
                }
            } else {
                items(
                    items = activeGames,
                    key = { it.id }
                ) { game ->
                    GameSummaryCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                        onDeleteClick = { gamePendingDelete = game }
                    )
                }
            }

            if (completedGames.isNotEmpty()) {
                item {
                    Text(
                        text = "Completed Games",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                items(
                    items = completedGames,
                    key = { it.id }
                ) { game ->
                    GameSummaryCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                        onDeleteClick = { gamePendingDelete = game }
                    )
                }
            }

            if (abandonedGames.isNotEmpty()) {
                item {
                    Text(
                        text = "Abandoned Games",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                items(
                    items = abandonedGames,
                    key = { it.id }
                ) { game ->
                    GameSummaryCard(
                        game = game,
                        onClick = { onGameClick(game.id) },
                        onDeleteClick = { gamePendingDelete = game }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            activeGames = listOf(
                GameSummary(
                    id = 1,
                    name = "Main Game",
                    remainingTime = 24.days + 6.hours,
                    deckCount = 38,
                    discardCount = 6,
                    lastDrawnCard = CardType.DOUBLE,
                    freezeEndsAt = null,
                    totalDays = 5
                ),
                GameSummary(
                    id = 2,
                    name = "Hard Mode",
                    remainingTime = 24.days + 6.hours + 2.minutes,
                    deckCount = 52,
                    discardCount = 9,
                    lastDrawnCard = CardType.FREEZE,
                    freezeEndsAt = Clock.System.now() + 3.days,
                    totalDays = 117
                )
            ),
            completedGames = listOf(
                GameSummary(
                    id = 3,
                    name = "Completed Game",
                    remainingTime = 0.days,
                    deckCount = 38,
                    discardCount = 6,
                    lastDrawnCard = CardType.DOUBLE,
                    freezeEndsAt = null,
                    totalDays = 15
                ),
            ),
            abandonedGames = listOf(
                GameSummary(
                    id = 4,
                    name = "Abandoned Game",
                    remainingTime = 0.days,
                    deckCount = 38,
                    discardCount = 6,
                    lastDrawnCard = CardType.DOUBLE,
                    freezeEndsAt = null,
                    totalDays = 155
                ),
            ),
            onGameClick = {},
            onCreateNewGameClick = {},
            onDeleteGameClick = {}
        )
    }
}
