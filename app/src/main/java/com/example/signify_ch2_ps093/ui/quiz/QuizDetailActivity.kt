package com.example.signify_ch2_ps093.ui.quiz

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.databinding.ActivityQuizDetailBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class QuizDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizDetailBinding

    private var exoPlayer: SimpleExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val contentNameReceived = intent.getStringExtra("NAME")
        val contentLinkReceived = intent.getStringExtra("LINK")

        Log.d("DetailQuiz", "Data name yang diterima : $contentNameReceived")



        binding.quizTitle.text = contentNameReceived


        binding.btnLanjutkan.setOnClickListener {
            val fragmentPilgan = PilihanGandaFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragmentPilgan)
            transaction.addToBackStack(null)
            transaction.commit()
        }


        val playerView = findViewById<PlayerView>(R.id.playerView)

        exoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        val mediaItem = MediaItem.fromUri(Uri.parse(contentLinkReceived))
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