package com.example.signify_ch2_ps093.ui.materigrup

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class DetailMateriActivity : AppCompatActivity() {

    private var exoPlayer: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_materi)

        val receivedLink = intent.getStringExtra("SELECTED_LINK")
        val receivedTitle = intent.getStringExtra("SELECTED_TITLE")

        val TvTitle : TextView = findViewById(R.id.tv_title_materi)
        TvTitle.text = receivedTitle

        val playerView = findViewById<PlayerView>(R.id.playerView)

        exoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        val mediaItem = MediaItem.fromUri(Uri.parse(receivedLink))
        val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, "app-name")))
            .createMediaSource(mediaItem)

        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.prepare()
        exoPlayer?.play()
    }

    override fun onDestroy() {
        super.onDestroy()

        exoPlayer?.release()
    }
}
