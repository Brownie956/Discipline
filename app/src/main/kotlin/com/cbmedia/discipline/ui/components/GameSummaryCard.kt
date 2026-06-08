package com.cbmedia.discipline.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cbmedia.discipline.IceBlue
import com.cbmedia.discipline.IceText
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.model.GameSummary
import com.cbmedia.discipline.toUKFormat
import java.time.LocalDate

@Composable
fun GameSummaryCard(
    game: GameSummary,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFrozen = game.freezeEndsOn?.let { LocalDate.now() <= it } == true

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFrozen) IceBlue else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isFrozen) IceText else Color.Unspecified
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                if (isFrozen) {
                    AssistChip(
                        onClick = {},
                        label = {
                            Icon(
                                imageVector = Icons.Default.AcUnit,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text("Frozen")
                        },
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete game"
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "${game.remainingDays} days remaining",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Deck: ${game.deckCount} cards · Discarded: ${game.discardCount}",
                style = MaterialTheme.typography.bodyMedium
            )

            val lastCardText = game.lastDrawnCard?.displayName ?: "None yet"

            Text(
                text = "Last drawn: $lastCardText",
                style = MaterialTheme.typography.bodyMedium
            )

            if (isFrozen) {
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Frozen until ${game.freezeEndsOn.toUKFormat()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun EmptyGamesSummaryCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text = "No games yet",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Create a new game to get started.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GamesSummaryCardPreview() {
    GameSummaryCard(
        game = GameSummary(
            id = 1,
            name = "Example Game",
            remainingDays = 10,
            deckCount = 30,
            discardCount = 4,
            lastDrawnCard = CardType.ARCTIC,
            freezeEndsOn = null
        ),
        onClick = {},
        onDeleteClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun GamesSummaryCardFrozenPreview() {
    GameSummaryCard(
        game = GameSummary(
            id = 1,
            name = "Example Game",
            remainingDays = 10,
            deckCount = 30,
            discardCount = 4,
            lastDrawnCard = CardType.ARCTIC,
            freezeEndsOn = LocalDate.now().plusDays(3)
        ),
        onClick = {},
        onDeleteClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun EmptyGamesSummaryCardPreview() {
    EmptyGamesSummaryCard()
}