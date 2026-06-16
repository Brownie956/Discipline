package com.cbmedia.discipline.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Entity(tableName = "games")
data class GameEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val remainingMinutes: Long,
    val deck: String,
    val discardPile: String,
    val lastDrawnCard: String?,
    val lastDrawTime: Long?,
    val freezeEndsAt: Long?,
    val createdDate: Long,
    val status: String,
    val endedDate: Long?,
    val baseTimerMinutes: Long,
    val drawIntervalMinutes: Long,
)
