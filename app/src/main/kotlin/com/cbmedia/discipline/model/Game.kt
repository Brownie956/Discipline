package com.cbmedia.discipline.model

import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Game @OptIn(ExperimentalTime::class) constructor(
    val id: Long,
    val name: String,
    val state: GameState,
    val createdDate: Instant,
    val baseTimer: Duration,
    val drawInterval: Duration,
    val status: GameStatus = GameStatus.ACTIVE,
    val endedDate: Instant? = null,
)
