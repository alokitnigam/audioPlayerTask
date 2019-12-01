package com.example.perpuletask.Fragments.Adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perpuletask.DI.Models.AudioModel
import com.example.perpuletask.R

class AudioListAdapter :
    RecyclerView.Adapter<AudioListAdapter.ViewHolder>() {
    private lateinit var itemClickListener: ItemClickListener
    var list: ArrayList<AudioModel> = ArrayList()


    fun setList(list: List<AudioModel>)
    {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    public fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }


    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.audio_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }


    fun updateItemDownloadProgress(progress: Int, audioModel: AudioModel?) {
        var position = list.indexOf(audioModel)

        for (i in 0 until list.size) {
            if(audioModel?.itemId == list[i].itemId){
                position = i
            }
        }
        audioModel?.progress = progress
        list.set(position, audioModel!!)
        notifyItemChanged(position)






    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(list[adapterPosition])
            }
        }
        val name: TextView = itemView.findViewById(R.id.desc)
        val progress: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind(audio: AudioModel?) {
            showRepoData(audio!!)

        }

        private fun showRepoData(audio: AudioModel) {
            name.setText(audio.desc)
            if(audio.progress != null)
                progress.setProgress(audio.progress!!)
        }





    }

    interface ItemClickListener{
        fun onItemClick(audioModel: AudioModel?)
    }

}