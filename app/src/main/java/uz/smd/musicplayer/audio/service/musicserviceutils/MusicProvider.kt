package uz.smd.musicplayer.audio.service.musicserviceutils

import android.os.Environment
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.util.Log
import java.io.File
import java.lang.Exception
import java.util.concurrent.Executors

// Music will be the same, they are pre-downloaded into the assets folder
object MusicProvider {

    var uriList =getPath()

    var i = 0
    val mediaMetadataList = uriList?.map {
        MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, it["file_path"])
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, it["file_name"])
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, "NoName")
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, "POP")
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "s${++i}")
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 44000L)
                .build()
    }
    val mediaMetadataMap = HashMap<String, MediaMetadataCompat>().apply {
        mediaMetadataList?.forEach {
            this[it.description.mediaId!!] = it
        }
    }
    val mediaItemList = mediaMetadataList?.map {
        MediaBrowserCompat.MediaItem(it.description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }
    val mediaGenreDescription = arrayListOf(
            MediaDescriptionCompat.Builder().setMediaId("Musics")
                    .setTitle("Musics on your phone")
                    .build()
    )
    val genreMediaItemList = mediaGenreDescription.map {
        MediaBrowserCompat.MediaItem(it, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE)
    }
    var x = 0
    val treeMap =
            HashMap<String, MediaNode<MediaBrowserCompat.MediaItem>>().apply {
                this["root"] = MediaNode(
                        MediaBrowserCompat.MediaItem(
                                MediaDescriptionCompat.Builder().setMediaId("root").setTitle("root").build(),
                                MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
                        )
                )
                genreMediaItemList.forEach {
                    this[it.mediaId ?: "${++x}"] = MediaNode(it, this["root"])
                    mediaItemList?.forEach { mediaItem ->
                        this[mediaItem.mediaId!!] = MediaNode(mediaItem, this[it.mediaId ?: "$x"])
                        this[it.mediaId ?: "$x"]!!.children.add(this[mediaItem.mediaId!!]!!)
                    }
                    this["root"]!!.children.add(this[it.mediaId ?: "$x"]!!)
                }
            }
}
//var uriList1= mutableListOf<Pair<String,String>>()

fun getPlayList(rootPath: String?): ArrayList<HashMap<String, String>>? {
    val fileList: ArrayList<HashMap<String, String>> = ArrayList()
    return try {
        val rootFolder = File(rootPath)
        val files: Array<File> =
            rootFolder.listFiles() //here you will get NPE if directory doesn't contains  any file,handle it like this.
        for (file in files) {
            if (file.isDirectory()) {
                if (getPlayList(file.getAbsolutePath()) != null) {
                    fileList.addAll(getPlayList(file.getAbsolutePath())!!)
                } else {
                    break
                }
            } else if (file.getName().endsWith(".mp3")) {
                val song: HashMap<String, String> = HashMap()
                song["file_path"] = file.getAbsolutePath()
                song["file_name"] = file.getName()
                Log.e("TTT", " name =${file.getName()} path = ${file.getAbsolutePath()}")
//                uriList1.add(Pair(file.getAbsolutePath(),file.getName()))
                fileList.add(song)
            }
        }
        fileList
    } catch (e: Exception) {
        null
    }
}
fun getPath(): ArrayList<HashMap<String, String>>? {
//        val songList = getPlayList("/storage/sdcard1/")
//    Executors.newSingleThreadExecutor().execute {
       return getPlayList(Environment.getExternalStorageDirectory().getAbsolutePath())
//    }
    }