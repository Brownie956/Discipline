package com.cbmedia.discipline.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM games WHERE status = 'ACTIVE' ORDER BY createdDate DESC")
    fun observeActiveGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE status = 'COMPLETED' ORDER BY createdDate DESC")
    fun observeCompletedGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE status = 'ABANDONED' ORDER BY createdDate DESC")
    fun observeAbandonedGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :gameId")
    fun observeGame(gameId: Long): Flow<GameEntity?>

    @Insert
    suspend fun insertGame(game: GameEntity): Long

    @Update
    suspend fun updateGame(game: GameEntity)
}
