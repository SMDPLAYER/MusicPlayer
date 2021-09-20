package uz.smd.musicplayer

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.smd.musicplayer.fragmnets.FavouriteFragment
import uz.smd.musicplayer.fragmnets.HomeFragment
import uz.smd.musicplayer.models.MusicFile
import nl.joery.animatedbottombar.AnimatedBottomBar
import uz.smd.musicplayer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val viewBinding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind,R.id.activityMain)
    private val TAG = "MainActivity"

    private val REQUEST_CODE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (checkForPermission()) {
            init()
        } else {
            askForPermission()
        }
    }

    private fun init() {
        getAllMusicFiles()
        initTabs()
    }

    private fun initTabs() {

        var fragmentList = listOf(HomeFragment(), FavouriteFragment())
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, fragmentList[0]).commit()
        viewBinding.bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                supportFragmentManager.beginTransaction().replace(R.id.framelayout, fragmentList[newIndex]).commit()
            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {

            }
        })
    }

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (checkForPermission()) {
                init()
            } else {
                Toast.makeText(this, resources.getString(R.string.allow_permission), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkForPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        } else {
            true
        }
    }

    private fun getAllMusicFiles() {
        var musicList = ArrayList<MusicFile>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projections = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media._ID,
        )
        var cursor = contentResolver.query(uri, projections, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                var title = cursor.getString(0)
                var album = cursor.getString(1)
                var duration = cursor.getString(2)
                var artist = cursor.getString(3)
                var data = cursor.getString(4)
                var id = cursor.getString(5)

                var musicFile = MusicFile(title, album, duration, artist, data, getThumbnail(data) , id)
                musicList.add(musicFile)
            }
            AppController.musicList = musicList
            AppController.setBackUpList()
        } else {
            println("cursor is null")
        }
    }

    companion object {
        fun getThumbnail(uri: String): Bitmap? {
            val mmr = MediaMetadataRetriever()
            val rawArt: ByteArray?
            var art: Bitmap? = null
            val bfo = BitmapFactory.Options()

            try {
                mmr.setDataSource(uri)
                rawArt = mmr.embeddedPicture
                if (null != rawArt) art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.size, bfo)
                return art
            } catch (e: Exception) {
                return art
            }
        }
    }

}