package uz.smd.musicplayer.audio.ui

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import uz.smd.musicplayer.R
import uz.smd.musicplayer.audio.service.musicserviceutils.MediaNode

//import kotlinx.android.synthetic.main.item_media.view.*
//import mohamed.mohamedresume.R
//import mohamed.mohamedresume.intro.ui.adapters.BaseAdapter
//import mohamed.mohamedresume.mediaplayer.audio.service.musicserviceutils.MediaNode
//import mohamed.mohamedresume.extensions.GlideApp

class MediaAdapter(private val context: Context, private val nodeList: ArrayList<MediaNode<MediaBrowserCompat.MediaItem>>,
                   private val onMediaItemClicked: (mediaId: String) -> Unit) :
    BaseAdapter<MediaAdapter.MediaViewHolder, MediaNode<MediaBrowserCompat.MediaItem>>(context, nodeList) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(LayoutInflater.from(context).inflate(R.layout.item_media, parent, false))
    }


    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val node = nodeList[position].data
        holder.apply {
            imgMedia?.let {
//                GlideApp.with(context)
//                    .load(node.description.iconBitmap)
//                    .error(android.R.drawable.stat_notify_error)
//                    .circleCrop()
//                    .into(it)
            }
            tvName?.text = node.description.title ?: "unKnown"
            itemView.setOnClickListener { _ -> node.mediaId?.let { onMediaItemClicked(it) } }
        }
    }

    class MediaViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imgMedia = view.findViewById<ImageView>(R.id.imgMedia) ?: null
        val tvName = view.findViewById<TextView>(R.id.tvName) ?: null
    }
}