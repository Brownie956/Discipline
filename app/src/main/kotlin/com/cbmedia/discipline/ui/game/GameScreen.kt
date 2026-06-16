package com.cbmedia.discipline.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.cbmedia.discipline.daysActive
import com.cbmedia.discipline.isGameFrozen
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.describeLastDraw
import com.cbmedia.discipline.model.Game
import com.cbmedia.discipline.model.GameState
import com.cbmedia.discipline.model.GameStatus
import com.cbmedia.discipline.toDisplayText
import com.cbmedia.discipline.toUKFormat
import com.cbmedia.discipline.ui.components.CardFrequencyRow
import com.cbmedia.discipline.ui.components.GameInfoCard
import java.time.LocalDate
import java.util.Locale.getDefault
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun GameScreen(
    game: Game,
    onDrawCard: () -> Unit,
    onEndGameEarly: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = game.state
    val isGameActive = game.status == GameStatus.ACTIVE

    val isFrozen = isGameFrozen(state.freezeEndsAt)
    val intervalHasPassed = state.lastDrawTime?.let { lastDraw ->
        Clock.System.now() >= lastDraw + game.drawInterval
    } ?: true

    val canDraw = isGameActive &&
            state.deck.isNotEmpty() &&
            state.lastDrawTime != LocalDate.now() &&
            !isFrozen &&
            intervalHasPassed

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

                    Text(if (isFrozen && state.freezeEndsAt != null) "Frozen Until ${state.freezeEndsAt.toUKFormat()}" else "Draw Card")
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                if (isGameActive) {
                    Text(
                        text = "Time Remaining",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = game.state.remainingMinutes.minutes.toDisplayText(),
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
                Text(
                    text = "Total days - ${game.daysActive()}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                GameInfoCard(
                    card = state.lastDrawnCard,
                    cardDescription = state.lastDrawnCard?.describeLastDraw(state),
                )
            }

            item {
                Text(
                    text = "Cards remaining in deck - ${state.deck.size}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            val remainingDeckCounts = state.deck
                .groupingBy { it }
                .eachCount()

            items(
                items = CardType.entries.filter { remainingDeckCounts.containsKey(it) },
                key = { "deck_$it" }
            ) { cardType ->
                CardFrequencyRow(
                    card = cardType,
                    count = remainingDeckCounts.getValue(cardType)
                )
            }

            item {
                Text(
                    text = "Discard Pile - ${state.discardPile.size}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (state.discardPile.isEmpty()) {
                item {
                    Text(
                        text = "No discarded cards yet.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                val cardCounts = state.discardPile
                    .groupingBy { it }
                    .eachCount()

                items(
                    items = CardType.entries.filter { cardCounts.containsKey(it) },
                    key = { "discard_$it" }
                ) { cardType ->
                    CardFrequencyRow(
                        card = cardType,
                        count = cardCounts.getValue(cardType)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    val exampleGame = Game(
        id = 12,
        name = "No Chocolate",
        state = GameState(
            remainingMinutes = (12.days + 3.hours + 43.minutes).inWholeMinutes,
            deck = listOf(
                CardType.GREEN,
                CardType.RED,
                CardType.RED,
                CardType.RED,
                CardType.STICKY,
                CardType.ARCTIC
            ),
            discardPile = listOf(
                CardType.GREEN,
                CardType.RED,
                CardType.RED,
                CardType.RED,
                CardType.RED,
                CardType.STICKY,
                CardType.YELLOW,
                CardType.RESET,
                CardType.DOUBLE,
                CardType.FREEZE,
                CardType.ARCTIC,
            ),
            lastDrawnCard = CardType.DOUBLE,
            lastDrawTime = Clock.System.now() - 3.days,
            freezeEndsAt = null
        ),
        createdDate = Clock.System.now() - 3.days,
        baseTimer = 10.days,
        drawInterval = 15.minutes
    )

    MaterialTheme {
        GameScreen(
            game = exampleGame,
            onDrawCard = {},
            onEndGameEarly = {}
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun GameScreenFrozenPreview() {
    val exampleGame = Game(
        id = 12,
        name = "No Chocolate",
        state = GameState(
            remainingMinutes = (3.hours + 43.minutes).inWholeMinutes,
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
            lastDrawTime = Clock.System.now(),
            freezeEndsAt = Clock.System.now() + 3.days
        ),
        createdDate = Clock.System.now() - 13.days,
        baseTimer = 10.days,
        drawInterval = 15.minutes
    )

    MaterialTheme {
        GameScreen(
            game = exampleGame,
            onDrawCard = {},
            onEndGameEarly = {}
        )
    }
}

@OptIn(ExperimentalTime::class)
@Preview(showBackground = true)
@Composable
private fun GameScreenCompletedPreview() {
    val exampleGame = Game(
        id = 12,
        name = "No Chocolate",
        state = GameState(
            remainingMinutes = (43.minutes).inWholeMinutes,
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
            lastDrawTime = Clock.System.now() - 1.days,
            freezeEndsAt = null
        ),
        createdDate = Clock.System.now() - 112.days,
        status = GameStatus.COMPLETED,
        baseTimer = 10.days,
        drawInterval = 15.minutes
    )

    MaterialTheme {
        GameScreen(
            game = exampleGame,
            onDrawCard = {},
            onEndGameEarly = {}
        )
    }
}
