package com.cbmedia.discipline.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.GameSummary
import com.cbmedia.discipline.ui.components.EmptyGamesSummaryCard
import com.cbmedia.discipline.ui.components.GameSummaryCard
import java.time.LocalDate

@Composable
fun HomeScreen(
    games: List<GameSummary>,
    onGameClick: (Long) -> Unit,
    onCreateNewGameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Your Games",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            if (games.isEmpty()) {
                item {
                    EmptyGamesSummaryCard()
                }
            } else {
                items(
                    items = games,
                    key = { it.id }
                ) { game ->
                    GameSummaryCard(
                        game = game,
                        onClick = { onGameClick(game.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            games = listOf(
                GameSummary(
                    id = 1,
                    name = "Main Game",
                    remainingDays = 24,
                    deckCount = 38,
                    discardCount = 6,
                    lastDrawnCard = CardType.DOUBLE,
                    freezeEndsOn = null
                ),
                GameSummary(
                    id = 2,
                    name = "Hard Mode",
                    remainingDays = 41,
                    deckCount = 52,
                    discardCount = 9,
                    lastDrawnCard = CardType.FREEZE,
                    freezeEndsOn = LocalDate.now().plusDays(3)
                )
            ),
            onGameClick = {},
            onCreateNewGameClick = {}
        )
    }
}
