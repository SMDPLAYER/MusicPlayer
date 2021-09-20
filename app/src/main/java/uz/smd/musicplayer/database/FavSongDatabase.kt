package uz.smd.musicplayer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavSongModel::class], version = 1)
abstract class FavSongDatabase : RoomDatabase() {

    abstract fun songDao(): FavSongDao


    companion object {
        private var instance: FavSongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FavSongDatabase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context.applicationContext, FavSongDatabase::class.java, "fav_song_database")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance!!
        }
    }
}