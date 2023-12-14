package com.example.signify_ch2_ps093.ui.quiz

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.signify_ch2_ps093.data.network.ContentItem
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.FragmentPeragakanBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class PeragakanFragment : Fragment() {

    private lateinit var binding: FragmentPeragakanBinding

    private lateinit var practiceItem: ArrayList<ContentItem>
    private lateinit var exoPlayer: SimpleExoPlayer

    private var currentVideoIndex = 0

    companion object {
        private const val ARG_PRACTICE = "PRACTICE"

        fun newInstance(practiceItem: ArrayList<ContentItem>): PeragakanFragment {
            val fragment = PeragakanFragment()
            val args = Bundle().apply {
                putParcelableArrayList(ARG_PRACTICE, practiceItem)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelableArrayList<ContentItem>(ARG_PRACTICE)?.let { practiceItems ->
            practiceItem = practiceItems
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeragakanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        practiceItem = requireArguments().getParcelableArrayList(ARG_PRACTICE) ?: arrayListOf()

        binding.btnSubmit.setOnClickListener {
            navigateToNextQuestion()
        }

        initializePlayer()
    }

    private fun initializePlayer() {
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

        val playerView = binding.playerView
        playerView.player = exoPlayer

        val userLevel = UserPreference.getUserLevel(requireContext())
        val filteredPracticeArrayList = practiceItem.filter { it.levelContent == userLevel }

        val currentContent = filteredPracticeArrayList.getOrNull(currentVideoIndex)
        if (currentContent?.link != null) {
            val mediaItem = MediaItem.fromUri(Uri.parse(currentContent.link))
            val dataSourceFactory = DefaultDataSourceFactory(
                requireContext(),
                "user-agent"
            )
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaItem)
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    }

    private fun navigateToNextQuestion() {
        val userLevel = UserPreference.getUserLevel(requireContext())
        val filteredContent = practiceItem.filter { it.levelContent == userLevel }

        if (currentVideoIndex < filteredContent.size - 1) {
            currentVideoIndex++
            releasePlayer()
            initializePlayer()
        } else {
            // Implement the action when all practice questions are completed, if needed
        }
    }

    private fun releasePlayer() {
        exoPlayer.release()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
    }
}
