package com.cbmedia.discipline.model

import androidx.compose.ui.graphics.Color
import com.cbmedia.discipline.IceBlue
import com.cbmedia.discipline.IceBlueDark
import com.cbmedia.discipline.IceText

enum class CardType(
    val displayName: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val discardedRule: DiscardRule = DiscardRule.DISCARDED,
) {
    GREEN(
        displayName = "Green",
        primaryColor = Color(0xFF008500),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    RED(
        displayName = "Red",
        primaryColor = Color(0xFFA10000),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    STICKY(
        displayName = "Sticky",
        discardedRule = DiscardRule.RESHUFFLED,
        primaryColor = Color(0xFF690C0C),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    YELLOW(
        displayName = "Yellow",
        primaryColor = Color(0xFFEAEA00),
        secondaryColor = Color(0xFF000000)
    ),
    RESET(
        displayName = "Reset",
        primaryColor = Color(0xFF002748),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    DOUBLE(
        displayName = "Double",
        primaryColor = Color(0xFF670067),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    FREEZE(
        displayName = "Freeze",
        primaryColor = IceBlue,
        secondaryColor = IceText
    ),
    ARCTIC(
        displayName = "Arctic",
        discardedRule = DiscardRule.RESHUFFLED,
        primaryColor = IceBlueDark,
        secondaryColor = IceText
    )
}

enum class DiscardRule(val description: String) {
    DISCARDED("Card added to discard pile"),
    RESHUFFLED("Card shuffled back into the deck")
}
