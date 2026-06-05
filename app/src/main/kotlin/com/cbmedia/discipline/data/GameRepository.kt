package com.cbmedia.discipline.data

import com.cbmedia.discipline.data.local.GameDao
import com.cbmedia.discipline.data.local.GameEntity
import com.cbmedia.discipline.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameRepository(
    private val gameDao: GameDao
) {
    fun observeActiveGames(): Flow<List<Game>> {
        return gameDao.observeActiveGames()
            .map { entities -> entities.map(GameEntity::toGame) }
    }

    fun observeGame(gameId: Long): Flow<Game?> {
        return gameDao.observeGame(gameId)
            .map { entity -> entity?.toGame() }
    }

    suspend fun createGame(game: Game): Long {
        return gameDao.insertGame(game.toEntity().copy(id = 0))
    }

    suspend fun updateGame(game: Game) {
        gameDao.updateGame(game.toEntity())
    }
}


