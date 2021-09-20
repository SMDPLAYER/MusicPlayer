package uz.smd.musicplayer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_song_table")
class FavSongModel(@PrimaryKey(autoGenerate = true) val id: Int, @ColumnInfo(name = "song_id") val songId: String)