package com.cbmedia.discipline.model

import java.time.LocalDate

data class Game(
    val id: Long,
    val name: String,
    val state: GameState,
    val createdDate: LocalDate,
    val endedDate: LocalDate? = null
)
