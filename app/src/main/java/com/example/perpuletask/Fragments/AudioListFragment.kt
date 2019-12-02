package com.example.perpuletask.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.nasa.Base.BaseFragment
import com.example.perpuletask.DI.Models.AudioModel
import com.example.perpuletask.DI.Network.NetworkState
import com.example.perpuletask.Fragments.Adapters.AudioListAdapter
import com.example.perpuletask.MainActivity
import com.example.perpuletask.R
import com.example.perpuletask.Service.DownloadService
import com.example.perpuletask.Service.DownloadService.Companion.getVideoFilePath
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.audio_list_fragment.*
import kotlinx.android.synthetic.main.audio_player_fragment.*
import javax.inject.Inject


class AudioListFragment : BaseFragment<AudioListViewModel>() {

    companion object {
        fun newInstance() = AudioListFragment()
    }



    @Inject
    lateinit var adapter: AudioListAdapter
    @Inject
    lateinit var audioListViewModel : AudioListViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        audiolistrv.layoutManager = LinearLayoutManager(activity)

        setObservers()

        setUpListener()
    }

    private fun setUpListener() {
        adapter.setItemClickListener(object:AudioListAdapter.ItemClickListener{
            override fun onItemClick(audioModel: AudioModel?) {
                activity?.viewpager!!.currentItem = 1
                (activity as MainActivity )
                    .bus()?.send(audioModel as Any)
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun setObservers() {
        getViewModel().getAudiosFromDB().observe(this,
            Observer<List<AudioModel>> { t ->
                audiolistrv.adapter = adapter
                adapter.setList(t!!)
                if(t.isNotEmpty() && t[0].savedPath.isNullOrEmpty() && !
                    t[0].isDownloading )
                    downloadAudio(t[0])
            })

        getViewModel().getNetworkState().observe(this, Observer<NetworkState> { t ->
            when (t.status) {
                NetworkState.SUCESS -> {

                }
                NetworkState.FAILED -> {
                    Toast.makeText(activity,"Loading FAiled.Some Error Occured",Toast.LENGTH_LONG).show()
                }
            }
        })

        (activity as MainActivity)
            .busPlayer()?.toObservable()?.subscribe { t ->

                val nextPosition = adapter.list.indexOf(t)+1
                if(t is AudioModel){
                    if (t.savedPath.isNullOrEmpty() && !t.isDownloading)
                        downloadAudio(t)
                    if (adapter.list.size > nextPosition && adapter.list[nextPosition].savedPath.isNullOrEmpty() &&  !adapter.list[nextPosition].isDownloading)

                        downloadAudio(adapter.list.get(nextPosition))
                }
            }
    }

    override fun layoutRes(): Int {
        return R.layout.audio_list_fragment
    }

    override fun getViewModel(): AudioListViewModel {
        return audioListViewModel
    }

    fun downloadAudio(audioModel: AudioModel){
        audioListViewModel.updateDownloadStatus(audioModel)
        val intent = Intent(activity, DownloadService::class.java)
        intent.putExtra("audio", audioModel)
        intent.putExtra("receiver", DownloadReceiver(Handler()))

        activity?.startService(intent)

    }

    private inner class DownloadReceiver(handler: Handler?) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                val progress = resultData.getInt("progress") //get the progress
                val audioModel = resultData.getSerializable("audio") as AudioModel
                adapter.updateItemDownloadProgress(progress,audioModel)

                if (progress==100)
                    audioListViewModel.saveToDB(getVideoFilePath(activity!!,audioModel),audioModel)
            }
        }
    }










}
