package com.example.perpuletask.Fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perpuletask.DI.Models.AudioModel
import com.example.perpuletask.DI.Models.DataModel
import com.example.perpuletask.DI.Network.ApiService
import com.example.perpuletask.DI.Network.NetworkState
import com.example.perpuletask.DI.database.AudioRepo
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class AudioListViewModel@Inject
constructor(private val db: AudioRepo, private val service: ApiService) : ViewModel() {
    var audioList: LiveData<List<AudioModel>> = MutableLiveData()
    var netWorkResponse: MutableLiveData<NetworkState> = MutableLiveData()
    fun getAudioList() {
        service.getAudio()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<Response<DataModel>>() {
                override fun onSuccess(t: Response<DataModel>) {
                    netWorkResponse.value = NetworkState(NetworkState.SUCESS,"Success")
                    if(t.isSuccessful){
                        db.addSongs(t.body()?.data!!)

                    }else{
                        netWorkResponse.value = NetworkState(NetworkState.SUCESS,"Failed")

                    }

                }

                override fun onError(e: Throwable) {
                    netWorkResponse.value = NetworkState(NetworkState.FAILED,e.localizedMessage)
                }
            })
    }
    fun getAudiosFromDB():LiveData<List<AudioModel>>{
        getAudioList()
        audioList=db.getSongs()
        return audioList
    }

    fun getNetworkState(): MutableLiveData<NetworkState> {
        return netWorkResponse

    }

    fun saveToDB(filePath: String?, audioModel: AudioModel) {
        audioModel.savedPath=filePath
        db.updateAudioModel(audioModel)
    }
    fun updateDownloadStatus( audioModel: AudioModel) {
        audioModel.isDownloading = true
        db.updateAudioModel(audioModel)
    }


}
