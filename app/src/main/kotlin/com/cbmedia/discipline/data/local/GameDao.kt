package com.cbmedia.discipline.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM games WHERE endedDate IS NULL ORDER BY createdDate DESC")
    fun observeActiveGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :gameId")
    fun observeGame(gameId: Long): Flow<GameEntity?>

    @Insert
    suspend fun insertGame(game: GameEntity): Long

    @Update
    suspend fun updateGame(game: GameEntity)
}
