package com.cbmedia.discipline.model

import androidx.compose.ui.graphics.Color

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
        primaryColor = Color(0xFF460505),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    YELLOW(
        displayName = "Yellow",
        primaryColor = Color(0xFFCCCC00),
        secondaryColor = Color(0xFF000000)
    ),
    RESET(
        displayName = "Reset",
        primaryColor = Color(0xFF002748),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    DOUBLE(
        displayName = "Double",
        primaryColor = Color(0xFF480048),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    FREEZE(
        displayName = "Freeze",
        primaryColor = Color(0xFF396185),
        secondaryColor = Color(0xFF00344D)
    ),
    ARCTIC(
        displayName = "Arctic",
        discardedRule = DiscardRule.RESHUFFLED,
        primaryColor = Color(0xFF6C91C0),
        secondaryColor = Color(0xFF00344D)
    )
}

enum class DiscardRule(val description: String) {
    DISCARDED("Card added to discard pile"),
    RESHUFFLED("Card shuffled back into the deck")
}
