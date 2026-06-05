package com.cbmedia.discipline.model

import androidx.compose.ui.graphics.Color

enum class CardType(
    val displayName: String,
    val description: String,
    val primaryColor: Color,
    val secondaryColor: Color,
) {
    GREEN(
        displayName = "Green",
        description = "1 day subtracted from days remaining",
        primaryColor = Color(0xFF008500),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    RED(
        displayName = "Red",
        description = "3 days added to days remaining",
        primaryColor = Color(0xFFA10000),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    STICKY(
        displayName = "Sticky",
        description = "3 days added to days remaining",
        primaryColor = Color(0xFF460505),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    YELLOW(
        displayName = "Yellow",
        description = "3 additional RED cards added into deck",
        primaryColor = Color(0xFFCCCC00),
        secondaryColor = Color(0xFF000000)
    ),
    RESET(
        displayName = "Reset",
        description = "Deck reset and shuffled (excluding the RESET card)",
        primaryColor = Color(0xFF002748),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    DOUBLE(
        displayName = "Double",
        description = "Number of RED and YELLOW cards in deck doubled",
        primaryColor = Color(0xFF480048),
        secondaryColor = Color(0xFFFFFFFF)
    ),
    FREEZE(
        displayName = "Freeze",
        description = "Countdown and deck frozen",
        primaryColor = Color(0xFF396185),
        secondaryColor = Color(0xFF00344D)
    ),
    ARCTIC(
        displayName = "Arctic",
        description = "Countdown and deck frozen",
        primaryColor = Color(0xFF6C91C0),
        secondaryColor = Color(0xFF00344D)
    )
}
