package com.example.perpuletask.DI.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.perpuletask.DI.Models.AudioModel

@Database(entities = [AudioModel::class], version = 1,exportSchema = false)
abstract class AudioDatabase : RoomDatabase() {

    abstract fun audioDao(): AudioRepo

    companion object {
        val DATABASE_NAME = "apod.db"
    }


}
