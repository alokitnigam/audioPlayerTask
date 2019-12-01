package com.example.perpuletask.Service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.os.ResultReceiver
import com.example.perpuletask.DI.Models.AudioModel
import java.io.*
import java.net.URL

class DownloadService : IntentService("DownloadService") {
    var audioModel: AudioModel? = null
    override fun onHandleIntent(intent: Intent) {
        audioModel = intent.getSerializableExtra("audio") as AudioModel
        val urlToDownload = audioModel!!.audio
        val receiver =
            intent.getParcelableExtra<Parcelable>("receiver") as ResultReceiver
        try { //create url and connect
            val url = URL(urlToDownload)
            val connection = url.openConnection()
            connection.connect()
            // this will be useful so that you can show a typical 0-100% progress bar
            val fileLength = connection.contentLength
            // download the file
            val input: InputStream =
                BufferedInputStream(connection.getInputStream())
            val path = getVideoFilePath(this, audioModel)
            val output: OutputStream = FileOutputStream(path)
            val data = ByteArray(1024)
            var total: Long = 0
            var count: Int
            while (input.read(data).also { count = it } != -1) {
                total += count.toLong()
                // publishing the progress....
                val resultData = Bundle()
                resultData.putInt("progress", (total * 100 / fileLength).toInt())
                resultData.putSerializable("audio", audioModel)
                receiver.send(UPDATE_PROGRESS, resultData)
                output.write(data, 0, count)
            }
            // close streams
            output.flush()
            output.close()
            input.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val resultData = Bundle()
        resultData.putInt("progress", 100)
        resultData.putSerializable("audio", audioModel)
        receiver.send(UPDATE_PROGRESS, resultData)
    }

    companion object {
        const val UPDATE_PROGRESS = 8344
        @Throws(IOException::class)
        fun getVideoFilePath(
            context: Context,
            audioModel: AudioModel?
        ): String {
            val dir = context.getExternalFilesDir(null)
            return if (dir == null) {
                ""
            } else {
                val folder = File(dir.absolutePath + "/PerpuleAudios")
                folder.mkdirs()
                //Save the path as a string value
                val extStorageDirectory = folder.toString()
                //Create New file and name it Image2.PNG
                val file = File(extStorageDirectory, audioModel!!.desc)
                if (!file.exists()) {
                    file.createNewFile()
                }
                file.absolutePath
            }
        }
    }
}