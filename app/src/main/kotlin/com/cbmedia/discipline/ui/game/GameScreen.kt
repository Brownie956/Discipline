package com.cbmedia.discipline.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cbmedia.discipline.IceBlue
import com.cbmedia.discipline.IceText
import com.cbmedia.discipline.inverted
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.describeLastDraw
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameState
import com.cbmedia.discipline.model.GameStatus
import com.cbmedia.discipline.toUKFormat
import com.cbmedia.discipline.ui.components.DiscardPileRow
import com.cbmedia.discipline.ui.components.GameInfoCard
import java.time.LocalDate
import java.util.Locale.getDefault

@Composable
fun GameScreen(
    game: Game,
    onDrawCard: () -> Unit,
    onEndGameEarly: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = game.state
    val isGameActive = game.status == GameStatus.ACTIVE

    val isFrozen = state.freezeEndsOn?.let { LocalDate.now() <= it } == true

    val canDraw = isGameActive &&
            state.deck.isNotEmpty() &&
            state.lastDrawDate != LocalDate.now() &&
            !isFrozen

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onDrawCard,
                    enabled = canDraw,
                    colors = if (isFrozen) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = IceBlue,
                            disabledContentColor = IceText
                        )
                    } else {
                        ButtonDefaults.buttonColors()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (isFrozen) Icons.Default.AcUnit else Icons.Default.Style,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(if (isFrozen) "Frozen Until ${state.freezeEndsOn.toUKFormat()}" else "Draw Card")
                }

                OutlinedButton(
                    onClick = onEndGameEarly,
                    enabled = isGameActive,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("End Game Early")
                }
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                if (isGameActive) {
                    Text(
                        text = "Days Remaining",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = state.remainingDays.toString(),
                        style = MaterialTheme.typography.displayLarge
                    )
                } else {
                    Text(
                        text = "Game ${game.status.toString().lowercase(getDefault())}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            item {
                GameInfoCard(
                    card = state.lastDrawnCard,
                    cardDescription = state.lastDrawnCard?.describeLastDraw(state),
                )
            }

            item {
                Row {
                    Text(
                        text = "Discard Pile - ${state.discardPile.size}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (state.discardPile.isEmpty()) {
                item {
                    Text(
                        text = "No discarded cards yet.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(state.discardPile) { card ->
                    DiscardPileRow(card)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    val exampleGame = Game(
        id = 12,
        name = "No Chocolate",
        state = GameState(
            remainingDays = 24,
            deck = listOf(
                CardType.GREEN,
                CardType.RED,
                CardType.STICKY,
                CardType.ARCTIC
            ),
            discardPile = listOf(
                CardType.GREEN,
                CardType.RED,
                CardType.STICKY,
                CardType.YELLOW,
                CardType.RESET,
                CardType.DOUBLE,
                CardType.FREEZE,
                CardType.ARCTIC,
            ),
            lastDrawnCard = CardType.DOUBLE,
            lastDrawDate = LocalDate.now().minusDays(1),
            freezeEndsOn = null
        ),
        createdDate = LocalDate.now()
    )

    MaterialTheme {
        GameScreen(
            game = exampleGame,
            onDrawCard = {},
            onEndGameEarly = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenFrozenPreview() {
    val exampleGame = Game(
        id = 12,
        name = "No Chocolate",
        state = GameState(
            remainingDays = 24,
            deck = listOf(
                CardType.GREEN,
                CardType.RED,
                CardType.STICKY,
                CardType.ARCTIC
            ),
            discardPile = listOf(
                CardType.GREEN,
                CardType.RED,
                CardType.STICKY,
                CardType.YELLOW,
                CardType.RESET,
                CardType.DOUBLE,
                CardType.FREEZE,
                CardType.ARCTIC,
            ),
            lastDrawnCard = CardType.FREEZE,
            lastDrawDate = LocalDate.now(),
            freezeEndsOn = LocalDate.now().plusDays(3)
        ),
        createdDate = LocalDate.now()
    )

    MaterialTheme {
        GameScreen(
            game = exampleGame,
            onDrawCard = {},
            onEndGameEarly = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenCompletedPreview() {
    val exampleGame = Game(
        id = 12,
        name = "No Chocolate",
        state = GameState(
            remainingDays = 24,
            deck = listOf(
                CardType.GREEN,
                CardType.RED,
                CardType.STICKY,
                CardType.ARCTIC
            ),
            discardPile = listOf(
                CardType.GREEN,
                CardType.RED,
                CardType.STICKY,
                CardType.YELLOW,
                CardType.RESET,
                CardType.DOUBLE,
                CardType.FREEZE,
                CardType.ARCTIC,
            ),
            lastDrawnCard = CardType.DOUBLE,
            lastDrawDate = LocalDate.now().minusDays(1),
            freezeEndsOn = null
        ),
        createdDate = LocalDate.now(),
        status = GameStatus.COMPLETED
    )

    MaterialTheme {
        GameScreen(
            game = exampleGame,
            onDrawCard = {},
            onEndGameEarly = {}
        )
    }
}
