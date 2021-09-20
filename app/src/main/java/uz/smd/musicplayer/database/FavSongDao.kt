package uz.smd.musicplayer.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavSongDao {

    @Query("SELECT * FROM fav_song_table")
    fun getAll(): List<FavSongModel>

    @Insert
    fun insert(favSongModel: FavSongModel)

    @Delete
    fun delete(favSongModel: FavSongModel)

}