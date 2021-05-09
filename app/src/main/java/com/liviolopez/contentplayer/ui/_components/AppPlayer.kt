package com.liviolopez.contentplayer.ui._components

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.liviolopez.contentplayer.ui.home.HomeFragment
import com.liviolopez.contentplayer.utils.extensions.setGone
import com.liviolopez.contentplayer.utils.extensions.visibleIf
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class AppPlayer(context: Context) {
    private val TAG = "AppPlayer"

    interface OnProgressListener {
        fun onUpdatePosition(currentPosition: Long)
    }

    var player: SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()
    private var dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory().setUserAgent(
        Util.getUserAgent(context, context.packageName)
    )

    var currentPlayerView: PlayerView? = null
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

    fun setTrackAndPlay(playerView: PlayerView, videoItem: HomeFragment.VideoItem, position: Long) {
        currentPlayerView = playerView

        if(player.isPlaying) player.stop()

        // playerView.setOnTouchListener(videoViewTouchPlayBack)
        playerView.player = null
        playerView.player = player
        // playerView.useController = false

        getMediaSource(videoItem)?.let {
            player.setMediaSource(it)
            player.prepare()
            player.seekTo(position)
            play()
        }
    }

    private fun getMediaSource(videoItem: HomeFragment.VideoItem): MediaSource? {
        return when (videoItem.format) {
            MimeTypes.APPLICATION_MP4 -> getProgressiveMediaItem(videoItem.url)
            MimeTypes.APPLICATION_M3U8 -> getHlsMediaItem(videoItem.url)
            MimeTypes.APPLICATION_MPD -> getDashMediaItem(videoItem.url)
            else -> null.also { Log.e(TAG, "Unknown media format (${videoItem.format})") }
        }
    }

    private fun getProgressiveMediaItem(videoUrl: String) = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
        MediaItem.Builder()
            .setUri(videoUrl.toUri())
            .build()
    )

    private fun getHlsMediaItem(videoUrl: String): MediaSource? {
        return null
    }


    private fun getDashMediaItem(videoUrl: String): MediaSource? {
        return null
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
}