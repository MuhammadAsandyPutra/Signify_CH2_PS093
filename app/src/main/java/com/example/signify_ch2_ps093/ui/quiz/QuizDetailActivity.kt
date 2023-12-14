package com.example.signify_ch2_ps093.ui.quiz

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.network.ContentItem
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.ActivityQuizDetailBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class QuizDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizDetailBinding

    private var exoPlayer: SimpleExoPlayer? = null
    private var contentList: ArrayList<ContentItem>? = null
    private var materialArrayList: ArrayList<ContentItem>? = null
    private var essayArrayList: ArrayList<ContentItem>? = null
    private var peragakanArrayList: ArrayList<ContentItem>? = null

    private var currentIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan data dari intent
        contentList = intent.getParcelableArrayListExtra("MATERIAL")
        materialArrayList = intent.getParcelableArrayListExtra("MULTIPLE_CHOICES")
        essayArrayList = intent.getParcelableArrayListExtra("ESSAY")
        peragakanArrayList = intent.getParcelableArrayListExtra("PRACTICE")

        // Menentukan aksi klik pada tombol "Lanjutkan"
        binding.btnLanjutkan.setOnClickListener {
            exoPlayer?.stop()

            if (currentIndex + 1 < contentList!!.size) {
                currentIndex++
                playContent(currentIndex)
            } else {
                // Jika sudah mencapai akhir konten, tampilkan fragment PilihanGandaFragment
                val fragment = PilihanGandaFragment.newInstance(
                    materialArrayList ?: arrayListOf(),
                    essayArrayList ?: arrayListOf(),
                    peragakanArrayList ?: arrayListOf()
                )
                val bundle = Bundle().apply {
                    putParcelableArrayList("MULTIPLE_CHOICES", materialArrayList)
                    putParcelableArrayList("ESSAY", essayArrayList)
                    putParcelableArrayList("PRACTICE", peragakanArrayList)
                }
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        // Memeriksa apakah terdapat konten dan memulai pemutaran jika ada
        if (!contentList.isNullOrEmpty()) {
            playContent(currentIndex)
        }
    }

    private fun playContent(index: Int) {
        val currentContent = contentList?.get(index)

        currentContent?.let { content ->
            if (content.levelContent == UserPreference.getUserLevel(this)) {
                // Jika konten memiliki level yang sesuai, mulai pemutaran
                initializePlayer(content)
            } else {
                // Jika tidak, lanjut ke konten selanjutnya
                currentIndex++
                if (currentIndex < contentList!!.size) {
                    playContent(currentIndex)
                }
            }
        }
    }

    private fun initializePlayer(content: ContentItem) {
        binding.quizTitle.text = content.name

        val playerView = findViewById<PlayerView>(R.id.playerView)

        exoPlayer?.release()
        exoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        val mediaItem = MediaItem.fromUri(Uri.parse(content.link))
        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "app-name"))
        ).createMediaSource(mediaItem)

        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.prepare()
        exoPlayer?.play()

        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    currentIndex++
                    if (currentIndex < contentList!!.size) {
                        playContent(currentIndex)
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }
}
