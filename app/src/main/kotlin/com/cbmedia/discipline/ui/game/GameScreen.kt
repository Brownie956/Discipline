package com.cbmedia.discipline.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.GameState
import com.cbmedia.discipline.toUKFormat
import com.cbmedia.discipline.ui.components.DiscardPileRow
import com.cbmedia.discipline.ui.components.GameInfoCard
import java.time.LocalDate

@Composable
fun GameScreen(
    state: GameState,
    onDrawCard: () -> Unit,
    onEndGameEarly: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFrozen = state.freezeEndsOn?.let { LocalDate.now() <= it } == true
    val canDraw = state.deck.isNotEmpty() &&
            state.lastDrawDate != LocalDate.now() &&
            !isFrozen

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = onDrawCard,
                    enabled = canDraw,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = IceBlue,
                        disabledContentColor = IceText
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (isFrozen) Icons.Default.AcUnit else Icons.Default.Style,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(if (isFrozen) "Frozen Until ${state.freezeEndsOn.toUKFormat()}" else "Draw Card")
                }

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onEndGameEarly,
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
                Text(
                    text = "Days Remaining",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = state.remainingDays.toString(),
                    style = MaterialTheme.typography.displayLarge
                )
            }

            item {
                GameInfoCard(
                    title = "Last Card Drawn",
                    card = state.lastDrawnCard?.displayName ?: "No card drawn yet",
                    cardDescription = state.lastDrawnCard?.description
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
    MaterialTheme {
        GameScreen(
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
            onDrawCard = {},
            onEndGameEarly = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenFrozenPreview() {
    MaterialTheme {
        GameScreen(
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
                lastDrawDate = LocalDate.now().minusDays(1),
                freezeEndsOn = LocalDate.now().plusDays(3)
            ),
            onDrawCard = {},
            onEndGameEarly = {}
        )
    }
}
