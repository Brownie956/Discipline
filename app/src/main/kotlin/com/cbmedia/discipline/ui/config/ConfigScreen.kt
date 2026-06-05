package com.cbmedia.discipline.ui.config

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cbmedia.discipline.model.CardType
import com.cbmedia.discipline.ui.components.CardCountSelector

@Composable
fun ConfigScreen(
    gameName: String,
    onGameNameChanged: (String) -> Unit,
    baseDays: String,
    onBaseDaysChanged: (String) -> Unit,
    useCardCountAsBaseDays: Boolean,
    onUseCardCountAsBaseDaysChanged: (Boolean) -> Unit,
    totalSelectedCards: Int,
    cardCounts: Map<CardType, Int>,
    onIncrement: (CardType) -> Unit,
    onDecrement: (CardType) -> Unit,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCards = remember(cardCounts) {
        cardCounts.values.sum()
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Surface(
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Total Cards: $totalCards",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = onStartGame,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = totalCards > 0
                    ) {
                        Text("Start Game")
                    }
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                OutlinedTextField(
                    value = gameName,
                    onValueChange = onGameNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Game Name")
                    },
                    placeholder = {
                        Text("e.g Summer Challenge")
                    },
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = if (useCardCountAsBaseDays) {
                        totalSelectedCards.toString()
                    } else {
                        baseDays
                    },
                    onValueChange = onBaseDaysChanged,
                    enabled = !useCardCountAsBaseDays,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Base Days")
                    },
                    placeholder = {
                        Text("30")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    supportingText = {
                        if (!useCardCountAsBaseDays && (baseDays.toIntOrNull() ?: 0) < 1) {
                            Text("Base days must be at least 1")
                        }
                    },
                    isError = !useCardCountAsBaseDays && (baseDays.toIntOrNull() ?: 0) < 1
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onUseCardCountAsBaseDaysChanged(!useCardCountAsBaseDays)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Checkbox(
                        checked = useCardCountAsBaseDays,
                        onCheckedChange = onUseCardCountAsBaseDaysChanged
                    )

                    Column {
                        Text(
                            text = "Use card count as base days",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "Base days will be $totalSelectedCards",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Cards",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            items(
                items = cardCounts.entries.toList(),
                key = { it.key }
            ) { (cardType, count) ->
                CardCountSelector(
                    title = cardType.displayName,
                    color = cardType.primaryColor,
                    count = count,
                    onIncrement = { onIncrement(cardType) },
                    onDecrement = { onDecrement(cardType) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfigScreenPreview() {
    val sampleCounts = mapOf(
        CardType.GREEN to 1,
        CardType.RED to 15,
        CardType.STICKY to 5,
        CardType.YELLOW to 2,
        CardType.RESET to 1,
        CardType.DOUBLE to 2,
        CardType.FREEZE to 10,
        CardType.ARCTIC to 3,
    )

    MaterialTheme {
        ConfigScreen(
            gameName = "Give up caffeine",
            onGameNameChanged = {},
            baseDays = "30",
            onBaseDaysChanged = {},
            useCardCountAsBaseDays = false,
            onUseCardCountAsBaseDaysChanged = {},
            totalSelectedCards = sampleCounts.values.sum(),
            cardCounts = sampleCounts,
            onIncrement = {},
            onDecrement = {},
            onStartGame = {}
        )
    }
}
