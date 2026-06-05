package com.cbmedia.discipline.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val remainingDays: Int,
    val deck: String,
    val discardPile: String,
    val lastDrawnCard: String?,
    val lastDrawDate: Long?,
    val freezeEndsOn: Long?,
    val createdDate: Long,
    val endedDate: Long?
)
