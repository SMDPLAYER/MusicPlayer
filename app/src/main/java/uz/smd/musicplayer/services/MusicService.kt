package uz.smd.musicplayer.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import uz.smd.musicplayer.AppController
import uz.smd.musicplayer.interfaces.PlayerInterface

import uz.smd.musicplayer.models.MusicFile

class MusicService : Service() {

    private val TAG = "MusicService"

    var binder = MyBinder()
    var mediaPlayer: MediaPlayer? = MediaPlayer()
    var musicList = ArrayList<MusicFile>()
    lateinit var uri: Uri
    private lateinit var playerInterface : PlayerInterface

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var action = intent?.action
        if (action != null) {
            handleAction(action)
        }
        return START_STICKY
    }

    private fun handleAction(action: String) {
        if (playerInterface != null) {
            when (action) {
                AppController.ACTION_NEXT -> {
                    playerInterface.nextSong()
                }
                AppController.ACTION_PREVIOUS -> {
                    playerInterface.previousSong()
                }
                AppController.ACTION_PLAY_PAUSE -> {
                    playerInterface.musicPlayPause()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        musicList = AppController.musicList
    }

    fun setPlayerInterface(playerInterface : PlayerInterface){
        this.playerInterface = playerInterface
    }
}