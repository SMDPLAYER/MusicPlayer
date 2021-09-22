package uz.smd.musicplayer.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import uz.smd.musicplayer.AppController

import uz.smd.musicplayer.models.MusicFile
import uz.smd.musicplayer.PlayerActivity
import uz.smd.musicplayer.R

class MusicAdapter(private val musicList: ArrayList<MusicFile>, private val context: Activity?) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    private val TAG = "MusicAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.music_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val singleMusicFile = musicList.get(position)

        holder.title.text = singleMusicFile.title

        if (singleMusicFile.thumbnail != null)
            Glide.with(context!!).asBitmap().load(singleMusicFile.thumbnail!!).into(holder.musicArt)

        holder.itemView.setOnClickListener {
            AppController.currentListIndex = position
            context?.startActivity(Intent(context, PlayerActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val musicArt =itemView.findViewById<CircleImageView>(R.id.circle_image)
    }
}