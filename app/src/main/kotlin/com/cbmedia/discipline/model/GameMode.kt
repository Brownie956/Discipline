package com.cbmedia.discipline.model

enum class GameMode(val cardCounts: Map<CardType, Int>) {
    EASY(
        mapOf(
            CardType.GREEN to 5,
            CardType.RED to 5,
            CardType.STICKY to 1,
            CardType.YELLOW to 1,
            CardType.RESET to 1,
            CardType.DOUBLE to 2,
            CardType.FREEZE to 2,
            CardType.ARCTIC to 1,
        )
    ),
    MEDIUM(
        mapOf(
            CardType.GREEN to 2,
            CardType.RED to 8,
            CardType.STICKY to 1,
            CardType.YELLOW to 7,
            CardType.RESET to 1,
            CardType.DOUBLE to 2,
            CardType.FREEZE to 4,
            CardType.ARCTIC to 1,
        )
    ),
    HARD(
        mapOf(
            CardType.GREEN to 5,
            CardType.RED to 15,
            CardType.STICKY to 3,
            CardType.YELLOW to 10,
            CardType.RESET to 1,
            CardType.DOUBLE to 1,
            CardType.FREEZE to 5,
            CardType.ARCTIC to 2,
        )
    ),
    IMPOSSIBLE(
        mapOf(
            CardType.GREEN to 10,
            CardType.RED to 20,
            CardType.STICKY to 5,
            CardType.YELLOW to 13,
            CardType.RESET to 1,
            CardType.DOUBLE to 1,
            CardType.FREEZE to 10,
            CardType.ARCTIC to 5,
        )
    )
}