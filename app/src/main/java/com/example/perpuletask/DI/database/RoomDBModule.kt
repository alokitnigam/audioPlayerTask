package com.example.perpuletask.DI.database

import android.content.Context

import androidx.room.Room

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class RoomDBModule {

    @Singleton
    @Provides
    fun provideAudioDatabase(context: Context): AudioDatabase {
        return Room.databaseBuilder(
            context,
            AudioDatabase::class.java, AudioDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideAudioDao(audioDatabase: AudioDatabase): AudioRepo {
        return audioDatabase.audioDao()
    }
}