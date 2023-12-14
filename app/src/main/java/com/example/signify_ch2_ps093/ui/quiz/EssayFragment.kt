package com.example.signify_ch2_ps093.ui.quiz

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.network.ContentItem
import com.example.signify_ch2_ps093.data.pref.UserPreference
import com.example.signify_ch2_ps093.databinding.FragmentEssayBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class EssayFragment : Fragment() {

    private var _binding: FragmentEssayBinding? = null
    private val binding get() = _binding!!

    private var exoPlayer: SimpleExoPlayer? = null

    private lateinit var essayArrayList: ArrayList<ContentItem>
    private var currentVideoIndex = 0

    private lateinit var practiceItem: ArrayList<ContentItem>

    companion object {
        private const val ARG_ESSAY = "ESSAY"
        private const val ARG_PRACTICE = "PRACTICE"

        fun newInstance(essayArrayList: ArrayList<ContentItem>, practiceItem: ArrayList<ContentItem>): EssayFragment {
            val fragment = EssayFragment()
            val args = Bundle().apply {
                putParcelableArrayList(ARG_ESSAY, essayArrayList)
                putParcelableArrayList(ARG_PRACTICE, practiceItem)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelableArrayList<ContentItem>(ARG_ESSAY)?.let { essay ->
            essayArrayList = essay
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEssayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        essayArrayList = requireArguments().getParcelableArrayList(ARG_ESSAY) ?: arrayListOf()
        practiceItem = requireArguments().getParcelableArrayList(ARG_PRACTICE) ?: arrayListOf()

        binding.btnSubmit.setOnClickListener {
            navigateToNextQuestion()
        }

        if (essayArrayList.isNotEmpty()) {
            initializePlayer()
        }
    }

    private fun initializePlayer() {
        val userLevel = UserPreference.getUserLevel(requireContext())

        val filteredContent = essayArrayList.filter { it.levelContent == userLevel }
        if (filteredContent.isNotEmpty()) {
            exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

            val playerView = binding.playerView
            playerView.player = exoPlayer

            val currentContent = filteredContent[currentVideoIndex]
            val mediaItem = currentContent.link?.let { MediaItem.fromUri(Uri.parse(it)) }
            mediaItem?.let {
                val dataSourceFactory = DefaultDataSourceFactory(
                    requireContext(),
                    "user-agent"
                )
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(it)
                exoPlayer?.setMediaSource(mediaSource)
                exoPlayer?.prepare()
                exoPlayer?.playWhenReady = true
            }
        }
    }

    private fun navigateToNextQuestion() {
        val userLevel = UserPreference.getUserLevel(requireContext())
        val filteredContent = essayArrayList.filter { it.levelContent == userLevel }
        if (currentVideoIndex < filteredContent.size - 1) {
            currentVideoIndex++
            releasePlayer()
            initializePlayer()
        } else {
            replaceWithPeragakanFragment()
        }
    }

    private fun replaceWithPeragakanFragment() {
        val peragakanFragment = PeragakanFragment.newInstance(practiceItem)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, peragakanFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
        _binding = null
    }
}
