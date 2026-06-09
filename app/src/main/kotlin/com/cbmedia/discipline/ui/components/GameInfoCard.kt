package com.cbmedia.discipline.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.cbmedia.discipline.model.CardType

@Composable
fun GameInfoCard(
    card: CardType?,
    modifier: Modifier = Modifier,
    cardDescription: String? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = card?.primaryColor ?: MaterialTheme.colorScheme.surfaceVariant,
            contentColor = card?.secondaryColor ?: Color.Unspecified
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            Box {
                // Left hand side
                Column(
                    modifier = Modifier.align(Alignment.TopStart),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row {
                        Text(
                            text = "Last Card Drawn",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(
                        text = card?.name ?: "No card drawn yet",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                // Right hand side
                Column(
                    modifier = Modifier.align(Alignment.TopEnd),
                ) {
                    card?.discardedRule?.description?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.End,
                        )
                    }
                }
            }

            cardDescription?.let {
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
private fun GameInfoCardPreview(
    @PreviewParameter(CardTypePreviewProvider::class)
    cardType: CardType
)
{
    MaterialTheme {
        GameInfoCard(
            card = cardType,
            cardDescription = "Example card description",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GameInfoCardNoCardPreview() {
    MaterialTheme {
        GameInfoCard(
            card = null,
            modifier = Modifier.padding(16.dp)
        )
    }
}

class CardTypePreviewProvider : PreviewParameterProvider<CardType> {
    override val values = CardType.entries.asSequence()
}
