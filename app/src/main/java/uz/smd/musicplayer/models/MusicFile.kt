package uz.smd.musicplayer.models

import android.graphics.Bitmap

data class MusicFile(val title: String?, val album: String?, val duration: String?, val artist: String?, val data: String?, val thumbnail: Bitmap? , val id : String)