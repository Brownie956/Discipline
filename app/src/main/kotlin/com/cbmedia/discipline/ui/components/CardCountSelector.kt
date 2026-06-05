package com.cbmedia.discipline.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CardCountSelector(
    title: String,
    color: Color,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Colour indicator
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(color, CircleShape)
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            StepperButton(
                text = "-",
                onClick = onDecrement
            )

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .widthIn(min = 40.dp),
                textAlign = TextAlign.Center
            )

            StepperButton(
                text = "+",
                onClick = onIncrement
            )
        }
    }
}

@Composable
private fun StepperButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .width(64.dp),
        shape = RoundedCornerShape(percent = 50),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardCountSelectorPreview() {
    CardCountSelector(
        title = "Red Cards",
        color = Color.Red,
        count = 10,
        onIncrement = {},
        onDecrement = {}
    )
}