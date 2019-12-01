package com.example.perpuletask.DI.Models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.io.Serializable

@Entity
data class AudioModel(
    @PrimaryKey
    var itemId : String,
    val desc: String?,
    val audio: String?,
    var savedPath:String?,
    var progress:Int? = 0,
    var isDownloading:Boolean

):Serializable