package com.cbmedia.discipline.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class TimerUnit(
    val label: String,
    val minutesMultiplier: Long
) {
    MINUTES(label = "Minutes", minutesMultiplier = 1),
    HOURS(label = "Hours", minutesMultiplier = 60),
    DAYS(label = "Days", minutesMultiplier = 60 * 24)
}

@Composable
fun TimerInput(
    label: String,
    amountText: String,
    selectedUnit: TimerUnit,
    onAmountChange: (String) -> Unit,
    onUnitChange: (TimerUnit) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = amountText,
            onValueChange = { newValue ->
                onAmountChange(newValue.filter { it.isDigit() })
            },
            enabled = enabled,
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimerUnit.entries.forEach { unit ->
                FilterChip(
                    selected = selectedUnit == unit,
                    onClick = { onUnitChange(unit) },
                    label = { Text(unit.name) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerInputPreview() {
    TimerInput(
        label = "Base timer",
        amountText = "30",
        selectedUnit = TimerUnit.MINUTES,
        enabled = true,
        onAmountChange = {},
        onUnitChange = {}
    )
}
