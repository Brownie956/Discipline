package com.cbmedia.discipline.model

import androidx.compose.ui.graphics.Color

enum class CardType(
    val displayName: String,
    val description: String,
    val uiColor: Color
) {
    GREEN(
        displayName = "Green",
        description = "1 day subtracted from days remaining",
        uiColor = Color(0xFF008500)
    ),
    RED(
        displayName = "Red",
        description = "3 days added to days remaining",
        uiColor = Color(0xFFA10000)
    ),
    STICKY(
        displayName = "Sticky",
        description = "3 days added to days remaining",
        uiColor = Color(0xFF460505)
    ),
    YELLOW(
        displayName = "Yellow",
        description = "3 additional RED cards added into deck",
        uiColor = Color(0xFFCCCC00)
    ),
    RESET(
        displayName = "Reset",
        description = "Deck reset and shuffled (excluding the RESET card)",
        uiColor = Color(0xFF002748)
    ),
    DOUBLE(
        displayName = "Double",
        description = "Number of RED and YELLOW cards in deck doubled",
        uiColor = Color(0xFF480048)
    ),
    FREEZE(
        displayName = "Freeze",
        description = "Countdown and deck frozen",
        uiColor = Color(0xFF396185)
    ),
    ARCTIC(
        displayName = "Arctic",
        description = "Countdown and deck frozen",
        uiColor = Color(0xFF6C91C0)
    )
}
