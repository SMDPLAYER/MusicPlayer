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
import uz.smd.musicplayer.R
import uz.smd.musicplayer.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewBinding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)

    private  val TAG = "HomeFragment"




    override fun onResume() {
        super.onResume()
        AppController.setMusicListForHome()
        initList()
    }

    private fun initList() {
        var musicAdapter = MusicAdapter(AppController.musicList, requireActivity())
        viewBinding.recyclerview.setHasFixedSize(true)
        viewBinding.recyclerview.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerview.adapter = musicAdapter
    }
}