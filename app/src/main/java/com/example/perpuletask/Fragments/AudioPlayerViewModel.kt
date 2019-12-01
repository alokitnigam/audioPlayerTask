package com.example.perpuletask.Fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perpuletask.DI.Models.AudioModel
import com.example.perpuletask.DI.Network.ApiService
import com.example.perpuletask.DI.database.AudioRepo
import javax.inject.Inject

class AudioPlayerViewModel @Inject constructor(private val db: AudioRepo): ViewModel()  {
    // TODO: Implement the ViewModel

    var audioList: LiveData<List<AudioModel>> = MutableLiveData()
    fun getAudiosFromDB():LiveData<List<AudioModel>>{
        audioList=db.getSongs()
        return audioList
    }
}
