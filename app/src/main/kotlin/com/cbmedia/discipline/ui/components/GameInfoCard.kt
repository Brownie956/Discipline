package com.cbmedia.discipline.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GameInfoCard(
    title: String,
    card: String,
    modifier: Modifier = Modifier,
    cardDescription: String? = null
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = card,
                style = MaterialTheme.typography.headlineSmall
            )

            cardDescription?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameInfoCardPreview() {
    MaterialTheme {
        GameInfoCard(
            title = "Last Card Drawn",
            card = "Double",
            cardDescription = "Number of RED and YELLOW cards in the deck doubled",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameInfoCardNoDescPreview() {
    MaterialTheme {
        GameInfoCard(
            title = "Last Card Drawn",
            card = "Double",
            modifier = Modifier.padding(16.dp)
        )
    }
}