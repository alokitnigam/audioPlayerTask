package com.example.perpuletask.DI.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.perpuletask.DI.Models.AudioModel

@Dao
interface AudioRepo{


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSongs(songsList:List<AudioModel>)

    @Update
    fun updateAudioModel(songsList:AudioModel)



    @Query("Select * from AudioModel")
    fun getSongs() : LiveData<List<AudioModel>>
}
