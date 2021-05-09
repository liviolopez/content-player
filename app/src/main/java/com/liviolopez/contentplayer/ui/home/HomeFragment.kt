package com.liviolopez.contentplayer.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.util.MimeTypes
import com.liviolopez.contentplayer.R
import com.liviolopez.contentplayer.databinding.FragmentHomeBinding
import com.liviolopez.contentplayer.ui._components.*
import com.liviolopez.contentplayer.utils._log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), AppPlayer.OnProgressListener {
    private val TAG = "SourceFragment"

    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    lateinit var appPlayer: AppPlayer

    data class VideoItem(val format:String, val url: String)

    val mapVideos = mapOf(
        "mp4" to VideoItem(MimeTypes.APPLICATION_MP4,"https://bitmovin-a.akamaihd.net/content/MI201109210084_1/MI201109210084_mpeg-4_hd_high_1080p25_10mbits.mp4"),
        "m3u8" to VideoItem(MimeTypes.APPLICATION_M3U8,"https://bitmovin-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        appPlayer = AppPlayer(requireContext())

        binding.apply {
            btPlayMp4.setOnClickListener { playContent(mapVideos["mp4"]!!) }
            btPlayM3u8.setOnClickListener { playContent(mapVideos["m3u8"]!!) }
        }
    }

    private fun playContent(videoItem: VideoItem){
        appPlayer.cleanView()

        appPlayer.onProgressListener = null

        appPlayer.apply {
            setTrackAndPlay(binding.vvItemVideo, videoItem, 0)
            onProgressListener = this@HomeFragment
            loadingBar = binding.loadingVideo
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        appPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        appPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        appPlayer.resume()
    }

    override fun onUpdatePosition(currentPosition: Long) {
        "current position: $currentPosition"._log()
    }
}