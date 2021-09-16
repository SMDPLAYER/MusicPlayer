package uz.smd.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Button
import com.google.android.exoplayer2.util.Log
import uz.smd.musicplayer.audio.service.musicserviceutils.MusicProvider
import uz.smd.musicplayer.audio.ui.TestActivity
import java.io.File
import java.lang.Exception

class PlayerActivity : AppCompatActivity() {

    private lateinit var mMediaBrowserCompat: MediaBrowserCompat
    private val connectionCallback: MediaBrowserCompat.ConnectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {

            // The browser connected to the session successfully, use the token to create the controller
            super.onConnected()
            mMediaBrowserCompat.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(this@PlayerActivity, token)
                MediaControllerCompat.setMediaController(this@PlayerActivity, mediaController)
            }
            playPauseBuild()
            Log.d("onConnected", "Controller Connected")
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            Log.d("onConnectionFailed", "Connection Failed")

        }

    }
    private val mControllerCallback = object : MediaControllerCompat.Callback() {
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        MusicProvider.genreMediaItemList
        startActivity(Intent(this, TestActivity::class.java))
        finish()
    }
    fun playPauseBuild() {
        val mediaController = MediaControllerCompat.getMediaController(this@PlayerActivity)
        findViewById<Button>(R.id.btn).setOnClickListener {
            Log.e("TTT", " Clicked")
            val state = mediaController.playbackState.state
            // if it is not playing then what are you waiting for ? PLAY !
            if (state == PlaybackStateCompat.STATE_PAUSED ||
                state == PlaybackStateCompat.STATE_STOPPED ||
                state == PlaybackStateCompat.STATE_NONE
            ) {

//                mediaController.transportControls.playFromUri(Uri.parse("asset:///heart_attack.mp3"), null)
                mediaController.transportControls.playFromUri(Uri.parse(getPath()), null)
                findViewById<Button>(R.id.btn).text = "Pause"
            }
            // you are playing ? knock it off !
            else if (state == PlaybackStateCompat.STATE_PLAYING ||
                state == PlaybackStateCompat.STATE_BUFFERING ||
                state == PlaybackStateCompat.STATE_CONNECTING
            ) {
                mediaController.transportControls.pause()

                findViewById<Button>(R.id.btn).text = "Play"
            }
        }
        mediaController.registerCallback(mControllerCallback)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val componentName = ComponentName(this, MusicService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mutableListOf<String>(android.Manifest.permission.READ_EXTERNAL_STORAGE).toTypedArray(),2)
        }
        // initialize the browser
        mMediaBrowserCompat = MediaBrowserCompat(
            this, componentName, //Identifier for the service
            connectionCallback,
            null
        )

    }

    override fun onStart() {
        super.onStart()
        // connect the controllers again to the session
        // without this connect() you won't be able to start the service neither control it with the controller
        mMediaBrowserCompat.connect()
    }

    override fun onStop() {
        super.onStop()
        // Release the resources
        val controllerCompat = MediaControllerCompat.getMediaController(this)
        controllerCompat?.unregisterCallback(mControllerCallback)
        mMediaBrowserCompat.disconnect()
    }


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
                    fileList.add(song)
                }
            }
            fileList
        } catch (e: Exception) {
            null
        }
    }
    fun getPath():String{
//        val songList = getPlayList("/storage/sdcard1/")
        val songList = getPlayList(Environment.getExternalStorageDirectory().getAbsolutePath())
        var filePath1=""
        songList?.forEachIndexed { index, hashMap ->
            val fileName = songList[index]["file_name"]
            val filePath = songList[index]["file_path"]
            Log.e("TTT", " name =$fileName path = $filePath")
            if (!filePath.isNullOrEmpty()){
                filePath1=filePath?:""
           return@forEachIndexed
            }
            //here you will get list of file name and file path that present in your device

        }
        return filePath1
    }


}