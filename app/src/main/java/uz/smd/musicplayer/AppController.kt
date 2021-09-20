package uz.smd.musicplayer

import android.app.Application
import android.content.Context
import uz.smd.musicplayer.models.MusicFile

class AppController : Application() {


    companion object {

        var channelId = "com.monik.musicplayer"
        var channelName = "Music Player Channel"

        var ACTION_PREVIOUS = "ACTION_PREVIOUS"
        var ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE"
        var ACTION_NEXT = "ACTION_NEXT"

        lateinit var context: Context
        var musicList = ArrayList<MusicFile>()
        var backUpList = ArrayList<MusicFile>()
        var currentListIndex = -1

        fun setRandomNumber() {
            val count = musicList.size
            val randomNumber = (0..count).random()
            currentListIndex = randomNumber
        }

        fun setBackUpList() {
            backUpList = musicList
        }

        fun setMusicListForHome() {
            musicList = backUpList
        }

        fun setMusicForFavourite(tempList: ArrayList<MusicFile>) {
            musicList = tempList
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}