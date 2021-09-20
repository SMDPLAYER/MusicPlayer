package uz.smd.musicplayer.fragmnets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.smd.musicplayer.AppController
import uz.smd.musicplayer.adapters.MusicAdapter
import uz.smd.musicplayer.database.FavSongDatabase
import uz.smd.musicplayer.models.MusicFile
import uz.smd.musicplayer.R
import uz.smd.musicplayer.databinding.FragmentFavouriteBinding


class FavouriteFragment : Fragment(R.layout.fragment_favourite) {
    private val viewBinding:FragmentFavouriteBinding by viewBinding(FragmentFavouriteBinding::bind)
    private val TAG = "FavouriteFragment"



    override fun onResume() {
        super.onResume()
        getFavList()
        initList()
    }

    private fun getFavList() {
        var filteredList = mutableListOf<MusicFile>()
        var favMusicList = activity?.let { FavSongDatabase.getInstance(it.baseContext).songDao().getAll() }
        var allMusicFile = AppController.musicList

        allMusicFile.forEach {
            var singleMusic = it
            favMusicList?.forEach {
                if (it.songId == singleMusic.id) {
                    filteredList.add(singleMusic)
                }
            }
        }

        AppController.setMusicForFavourite(filteredList as ArrayList<MusicFile>)
    }

    private fun initList() {
        var musicAdapter = MusicAdapter(AppController.musicList, requireActivity())
        viewBinding.recyclerview.setHasFixedSize(true)
        viewBinding.recyclerview.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerview.adapter = musicAdapter
    }

}