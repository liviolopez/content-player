package com.liviolopez.contentplayer.ui.home

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.exoplayer2.util.MimeTypes
import com.liviolopez.contentplayer.R
import com.liviolopez.contentplayer.databinding.FragmentHomeBinding
import com.liviolopez.contentplayer.ui._components.*
import com.liviolopez.contentplayer.utils._log
import com.liviolopez.contentplayer.utils.extensions.setOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), AppPlayer.OnProgressListener {
    private val TAG = "SourceFragment"

    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    lateinit var appPlayer: AppPlayer

    enum class DrmUuid { WIDEVINE, PLAYREADY, CLEARKEY }
    data class Drm(val license: String, val uuid: DrmUuid)
    data class VideoItem(val format:String, val url: String, val drm: Drm? = null)

    val mapVideos = mapOf(
        "mp4" to VideoItem(MimeTypes.VIDEO_MP4,"https://bitmovin-a.akamaihd.net/content/MI201109210084_1/MI201109210084_mpeg-4_hd_high_1080p25_10mbits.mp4"),
        "webm" to VideoItem(MimeTypes.VIDEO_WEBM, "https://dl8.webmfiles.org/big-buck-bunny_trailer.webm"),

        "m3u8" to VideoItem(MimeTypes.APPLICATION_M3U8,"https://bitmovin-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8"),
        "m3u8-drm" to VideoItem(
            MimeTypes.APPLICATION_M3U8,
            "https://bitmovin-a.akamaihd.net/content/art-of-motion_drm/m3u8s/11331.m3u8",
            Drm("https://widevine-proxy.appspot.com/proxy", DrmUuid.WIDEVINE)
        ),


        "smooth" to VideoItem(MimeTypes.APPLICATION_SS, "https://test.playready.microsoft.com/smoothstreaming/SSWSS720H264/SuperSpeedway_720.ism/manifest"),

        "dash" to VideoItem(MimeTypes.APPLICATION_MPD, "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd"),
        "dash-drm" to VideoItem(
            MimeTypes.APPLICATION_MPD,
            "https://bitmovin-a.akamaihd.net/content/art-of-motion_drm/mpds/11331.mpd",
            Drm("https://widevine-proxy.appspot.com/proxy", DrmUuid.WIDEVINE)
        ),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        appPlayer = AppPlayer(requireContext())

        setupFilters()
    }

    private fun setupFilters(){
        data class Format(val id: String, val description: String)
        val formats = listOf(
            Format("all","All"),
            Format("progressive","Progressive"),
            Format("m3u8","HLS: HTTP Live Streaming"),
            Format("mpd","DASH: Dynamic Adaptive Streaming over HTTP"),
            Format("smooth","Smooth Streaming")
        )

        binding.dmStreamType.setOptions(formats,
            show = { it.description },
            onClick = {  }
        )

        val drmTypes = listOf(
            "Anyone", "Without",
            "PlayReady","WideVine", "ClearKey"
        )
        binding.dmContentProtection.setOptions(drmTypes,
            show = { it },
            onClick = {  }
        )
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