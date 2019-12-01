package com.example.perpuletask.Fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.Observer
import com.app.nasa.Base.BaseFragment
import com.example.perpuletask.DI.Models.AudioModel
import com.example.perpuletask.MainActivity
import com.example.perpuletask.R
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.audio_list_fragment.*
import kotlinx.android.synthetic.main.audio_player_fragment.*
import javax.inject.Inject


class AudioPlayerFragment : BaseFragment<AudioPlayerViewModel>(),
    View.OnTouchListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnBufferingUpdateListener {
    private lateinit var currentPlayingSong: AudioModel
    private var mediaPlayer: MediaPlayer? = null
    private val handler: Handler = Handler()
    lateinit var allSongs:List<AudioModel>

    private var mediaFileLengthInMilliseconds = 0
    companion object {
        fun newInstance() = AudioPlayerFragment()
    }
    @Inject
    lateinit var audioPlayerViewModel: AudioPlayerViewModel
    private  var position= 0
    private lateinit var audioList :List<AudioModel>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnBufferingUpdateListener(this)
        mediaPlayer?.setOnCompletionListener(this)

        (activity as MainActivity)
            .bus()?.toObservable()?.subscribe(object : Consumer<Any>{
                override fun accept(t: Any?) {
                    if(t is AudioModel){
                        playSong(t)

                    }
                }

            })

        setupObserver()

        playpausebutton.setOnClickListener {
            mediaFileLengthInMilliseconds =
                mediaPlayer!!.duration // gets the song length in milliseconds from URL
            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer!!.start()

                playpausebutton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)
            } else {
                mediaPlayer!!.pause()
                playpausebutton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp)
            }
            primarySeekBarProgressUpdater()
        }

        next.setOnClickListener {
            if(allSongs.indexOf(currentPlayingSong)+1 < allSongs.size)
                playSong(allSongs[allSongs.indexOf(currentPlayingSong)+1])
        }
        previous.setOnClickListener {
            if(allSongs.indexOf(currentPlayingSong) -1> 0){

                playSong(allSongs[allSongs.indexOf(currentPlayingSong)-1])
            }else{
                mediaPlayer?.reset()
            }

        }

    }

    private fun playSong(t:AudioModel) {
        mediaPlayer?.reset()
        try {
            if (t.savedPath.isNullOrEmpty())
                mediaPlayer?.setDataSource(t.audio)
            else
                mediaPlayer?.setDataSource(t.savedPath)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            currentPlayingSong = t
            playpausebutton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp)
            if(allSongs.indexOf(currentPlayingSong)+1 <= allSongs.size)
                startDownloadnextSong(t)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupObserver() {
        getViewModel().getAudiosFromDB().observe(this,
            Observer<List<AudioModel>> { t ->
                allSongs = t
            })
    }

    private fun startDownloadnextSong(t: AudioModel) {
            (activity as MainActivity )
                .busPlayer()?.send(t)
    }


    override fun layoutRes(): Int {
        return R.layout.audio_player_fragment
    }

    override fun getViewModel(): AudioPlayerViewModel {
        return audioPlayerViewModel
    }


    private fun primarySeekBarProgressUpdater() {
        SeekBarTestPlay.setProgress((mediaPlayer!!.currentPosition.toFloat() / mediaFileLengthInMilliseconds * 100).toInt()) // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer!!.isPlaying) {
            val notification = Runnable { primarySeekBarProgressUpdater() }
            handler.postDelayed(notification, 1000)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (v!!.id == SeekBarTestPlay.id && mediaPlayer!!.isPlaying) {
            val sb = v as SeekBar
            val playPositionInMillisecconds =
                mediaFileLengthInMilliseconds / 100 * sb.progress
            mediaPlayer!!.seekTo(playPositionInMillisecconds)
        }
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playpausebutton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        SeekBarTestPlay.setSecondaryProgress(percent);
    }

    override fun onPause() {
        super.onPause()
        if(mediaPlayer!= null){
            mediaPlayer?.pause()
            playpausebutton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);

        }

    }


}
