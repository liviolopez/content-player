package com.liviolopez.contentplayer.ui._components

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import com.liviolopez.contentplayer.data.local.model.Item
import com.liviolopez.contentplayer.utils.extensions.setGone
import com.liviolopez.contentplayer.utils.extensions.visibleIf
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class AppPlayer(val context: Context) {
    private val TAG = "AppPlayer"

    interface OnProgressListener {
        fun onUpdatePosition(currentPosition: Long)
    }

    var player: SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()

    private var currentPlayerView: PlayerView? = null
    private var currentMediaItem: Item? = null

    var loadingBar: View? = null
    var onProgressListener: OnProgressListener? = null
    private val semaphoreListener = Semaphore(1)

    init {
        player.repeatMode = Player.REPEAT_MODE_ONE

        player.addListener(object: Player.EventListener{
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if(isPlaying) progressListener()
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)

                loadingBar?.visibleIf { isLoading && !player.isPlaying }
            }
        })
    }

    private fun play() {
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun release(){
        player.stop()
        player.release()

        currentPlayerView?.player = null
    }

    fun resume(){
        if(currentPlayerView?.player !== null) {
            player.playWhenReady = true
        }
    }

    fun cleanView(){
        currentPlayerView = null
    }

    fun setTrackAndPlay(playerView: PlayerView, item: Item, position: Long) {
        currentMediaItem = item
        currentPlayerView = playerView

        if(player.isPlaying) player.stop()

        // playerView.setOnTouchListener(videoViewTouchPlayBack)
        playerView.player = null
        playerView.player = player
        // playerView.useController = false

        getMediaSource()?.let {
            player.setMediaSource(it)
            player.prepare()
            player.seekTo(position)
            play()
        }
    }

    private fun getMediaSource(): MediaSource? {
        return if(currentMediaItem == null) null
        else {
            when (StreamType.from(currentMediaItem!!.format)) {
                StreamType.PROGRESSIVE -> getProgressiveMediaSource()
                StreamType.HLS -> getHlsMediaSource()
                StreamType.DASH -> getDashMediaSource()
                StreamType.SMOOTH -> getSmoothMediaSource()
                StreamType.RTSP -> getRtspMediaSource()

                else -> null.also { Log.e(TAG, "Unknown media format (${currentMediaItem!!.format})") }
            }
        }
    }

    private val dataSourceFactory = DefaultHttpDataSource
        .Factory()
        .setUserAgent(Util.getUserAgent(context, context.packageName))

    // ------ Stream Type Factories -------
    private fun getProgressiveMediaSource() = ProgressiveMediaSource
        .Factory(dataSourceFactory).createMediaSource(mediaItem())

    private fun getHlsMediaSource() = HlsMediaSource
        .Factory(dataSourceFactory).createMediaSource(mediaItem())

    private fun getDashMediaSource() = DashMediaSource
        .Factory(dataSourceFactory).createMediaSource(mediaItem())

    private fun getSmoothMediaSource() = SsMediaSource
        .Factory(dataSourceFactory).createMediaSource(mediaItem())

    private fun getRtspMediaSource() = RtspMediaSource
        .Factory().createMediaSource(mediaItem())
    // ------------------------------------

    private fun mediaItem(): MediaItem {
        val mediaItem = MediaItem.Builder()
        mediaItem.setUri(currentMediaItem!!.url.toUri())

        if(!currentMediaItem?.drmUuid.isNullOrBlank()){
            mediaItem.setDrmUuid(DrmType.from(currentMediaItem!!.drmUuid!!)?.uuid)
            mediaItem.setDrmLicenseUri(currentMediaItem!!.drmLicense)
        }

        return mediaItem.build()
    }

    private fun progressListener() {
        CoroutineScope(Dispatchers.IO).launch {
            semaphoreListener.withPermit {
                var isPlaying = true

                while (isPlaying) {
                    delay(1000)

                    withContext(Dispatchers.Main) {
                        Log.v(TAG, "Current video on position: ${player.contentPosition}")

                        if(player.isPlaying && loadingBar != null) loadingBar?.setGone()

                        onProgressListener?.onUpdatePosition(player.contentPosition)
                        isPlaying = player.isPlaying
                    }
                }
            }
        }
    }

    enum class StreamType {
        PROGRESSIVE, HLS, DASH, SMOOTH, RTSP;

        companion object {
            fun from(format: String) = values().firstOrNull {
                    it.formats.contains(format)
                }
        }

        val detailsName get() = when(this){
                PROGRESSIVE -> "Progressive"
                HLS -> "HLS: HTTP Live Streaming"
                DASH -> "DASH: Dynamic Adaptive Streaming over HTTP"
                SMOOTH -> "Smooth Streaming"
                RTSP -> "Real Time Streaming Protocol"
            }

        val formats get() = when(this){
                PROGRESSIVE -> listOf("mp4", "webm")
                HLS -> listOf("m3u8")
                DASH -> listOf("mpd")
                SMOOTH -> listOf("smooth")
                RTSP -> listOf("rtsp")
            }
    }

    enum class DrmType {
        PlayReady, WideVine, ClearKey;

        companion object {
            fun from(format: String) = values().firstOrNull {
                    it.name.lowercase() == format.lowercase()
                }
        }

        val uuid get() = when(this){
                PlayReady -> C.PLAYREADY_UUID
                WideVine -> C.WIDEVINE_UUID
                ClearKey -> C.CLEARKEY_UUID
            }
    }
}