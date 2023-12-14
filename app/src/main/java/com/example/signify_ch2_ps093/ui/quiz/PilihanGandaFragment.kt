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
import com.example.signify_ch2_ps093.databinding.FragmentPilihanGandaBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class PilihanGandaFragment : Fragment() {

    private var _binding: FragmentPilihanGandaBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedAnswers: Array<Int?>

    private var currentQuestionIndex = 0
    private var selectedAnswerIndex: Int = -1

    private lateinit var multipleChoices: ArrayList<ContentItem>
    private var exoPlayer: SimpleExoPlayer? = null

    private lateinit var essayArrayList: ArrayList<ContentItem>
    private lateinit var practiceItem: ArrayList<ContentItem>

    companion object {
        const val ARG_MULTIPLE_CHOICES = "MULTIPLE_CHOICES"
        const val ARG_ESSAY = "ESSAY"
        const val ARG_PRACTICE = "PRACTICE"

        fun newInstance(multipleChoices: ArrayList<ContentItem>, essayItem: ArrayList<ContentItem>, practiceItem: ArrayList<ContentItem>): PilihanGandaFragment {
            val fragment = PilihanGandaFragment()
            val args = Bundle().apply {
                putParcelableArrayList(ARG_MULTIPLE_CHOICES, multipleChoices)
                putParcelableArrayList(ARG_ESSAY, essayItem)
                putParcelableArrayList(ARG_PRACTICE, practiceItem)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelableArrayList<ContentItem>(ARG_MULTIPLE_CHOICES)?.let { choices ->
            multipleChoices = choices
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPilihanGandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showQuestionAndAnswers(currentQuestionIndex)

        selectedAnswers = Array(multipleChoices.size) { null }

        binding.btnSubmit.setOnClickListener {
            navigateToNextQuestion()
        }

        binding.btnA.setOnClickListener {
            selectedAnswerIndex = 0
            updateButtonState()
            selectedAnswers[currentQuestionIndex] = selectedAnswerIndex
        }

        binding.btnB.setOnClickListener {
            selectedAnswerIndex = 1
            updateButtonState()
            selectedAnswers[currentQuestionIndex] = selectedAnswerIndex
        }

        binding.btnC.setOnClickListener {
            selectedAnswerIndex = 2
            updateButtonState()
            selectedAnswers[currentQuestionIndex] = selectedAnswerIndex
        }

        binding.btnD.setOnClickListener {
            selectedAnswerIndex = 3
            updateButtonState()
            selectedAnswers[currentQuestionIndex] = selectedAnswerIndex
        }

        essayArrayList = requireArguments().getParcelableArrayList(ARG_ESSAY) ?: arrayListOf()
        practiceItem = requireArguments().getParcelableArrayList(ARG_PRACTICE) ?: arrayListOf()

        initializePlayer()
    }

    private fun initializePlayer() {
        val userLevel = UserPreference.getUserLevel(requireContext())

        val filteredContent = multipleChoices.filter { it.levelContent == userLevel }
        if (filteredContent.isNotEmpty()) {
            exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

            val playerView = binding.playerView
            playerView.player = exoPlayer

            val currentContent = filteredContent[currentQuestionIndex]
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

    private fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }

    private fun updateButtonState() {
        // Reset semua tombol ke tampilan semula
        binding.btnA.isPressed = false
        binding.btnB.isPressed = false
        binding.btnC.isPressed = false
        binding.btnD.isPressed = false

        // Atur tombol yang dipilih dengan tampilan tertekan
        when (selectedAnswerIndex) {
            0 -> binding.btnA.isPressed = true
            1 -> binding.btnB.isPressed = true
            2 -> binding.btnC.isPressed = true
            3 -> binding.btnD.isPressed = true
        }
    }

    private fun showQuestionAndAnswers(questionAnswer: Int) {
        // Tampilkan pertanyaan dan opsi jawaban
        val currentContent = multipleChoices[questionAnswer]
        binding.tvTitle.text = currentContent.name

        val options = currentContent.options
        binding.apply {
            btnA.text = options?.get(0)
            btnB.text = options?.get(1)
            btnC.text = options?.get(2)
            btnD.text = options?.get(3)
        }
    }

    private fun navigateToNextQuestion() {
        val userLevel = UserPreference.getUserLevel(requireContext())

        val filteredContent = multipleChoices.filter { it.levelContent == userLevel }

        if (currentQuestionIndex < filteredContent.size - 1) {
            currentQuestionIndex++
            selectedAnswerIndex = -1
            selectedAnswers[currentQuestionIndex] = selectedAnswerIndex
            releasePlayer()
            showQuestionAndAnswers(currentQuestionIndex)
            initializePlayer()
        } else {
            replaceWithEssayFragment()
        }
    }

    private fun replaceWithEssayFragment() {
        val essayFragment = EssayFragment.newInstance(essayArrayList, practiceItem)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, essayFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
        _binding = null
    }
}
