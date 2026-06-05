package com.cbmedia.discipline

import android.app.Application
import androidx.room.Room
import com.cbmedia.discipline.data.GameRepository
import com.cbmedia.discipline.data.local.AppDatabase

class DisciplineApplication : Application() {

    lateinit var gameRepository: GameRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "discipline.db"
        ).build()

        gameRepository = GameRepository(database.gameDao())
    }
}
