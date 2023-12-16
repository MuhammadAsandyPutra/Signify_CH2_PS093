package com.example.signify_ch2_ps093.ui.camera

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.databinding.ActivityCameraBinding
import com.example.signify_ch2_ps093.ui.quiz.QuizResultFragment
import com.example.signify_ch2_ps093.ui.utils.hide
import com.example.signify_ch2_ps093.ui.utils.show
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import java.text.SimpleDateFormat
import java.util.Locale


class CameraActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityCameraBinding
    private lateinit var player: SimpleExoPlayer
    private val recordingDuration = 5000
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var currentVideoUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        viewBinding.captureVideo.setOnClickListener {
            val delayInMillis = 2000
            Handler().postDelayed({
                if (recording == null) {
                    startRecording()
                } else {
                    stopRecording()
                }
            }, delayInMillis.toLong())
        }

        viewBinding.stopVideo.setOnClickListener {
            stopRecording()
        }

        viewBinding.btnGaleri.setOnClickListener {
            openGallery()
        }

        viewBinding.btnTips.setOnClickListener {
            showTipsDialog()
        }


    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.LOWEST))
            .build()
        videoCapture = VideoCapture.withOutput(recorder)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // Select front camera as a default
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun startRecording() {
        val videoCapture = this.videoCapture ?: return
        viewBinding.captureVideo.isEnabled = false

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        viewBinding.captureVideo.isEnabled = true
                        viewBinding.stopVideo.show()
                        viewBinding.btnGaleri.hide()
                        viewBinding.btnTips.hide()

                        Handler().postDelayed({
                            stopRecording()
                        }, recordingDuration.toLong())
                    }

                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            showPreview(recordEvent.outputResults.outputUri)
                        }
                        viewBinding.captureVideo.isEnabled = true
                        viewBinding.stopVideo.hide()
                    }
                }
            }
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
    }

    private fun showPreview(videoUri: Uri) {
        currentVideoUri = videoUri
        viewBinding.viewFinder.hide()
        viewBinding.cvPreviewVideo.show()
        viewBinding.playerView.show()
        viewBinding.btnLanjutkan.show()
        viewBinding.captureVideo.hide()
        viewBinding.btnGaleri.hide()
        viewBinding.btnTips.hide()
        viewBinding.ivCorrect.show()
        viewBinding.tvAnswerCorrect.show()

        viewBinding.btnLanjutkan.setOnClickListener {
            val fragment = QuizResultFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        initializePlayer(videoUri)
    }

    private fun initializePlayer(videoUri: Uri) {
        player = SimpleExoPlayer.Builder(this).build()
        player.setMediaItem(MediaItem.fromUri(videoUri))
        player.repeatMode = Player.REPEAT_MODE_ALL
        viewBinding.playerView.player = player
        player.prepare()
        player.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onResume() {
        super.onResume()
        currentVideoUri?.let { showPreview(it) }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentVideoUri = uri
            showVideo()
        } else {
            Log.d("Video Picker", "No media selected")
        }
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
    }

    private fun showVideo() {
        currentVideoUri?.let {
            Log.d("Video URI", "showVideo: $it")
            showPreview(it)
        }
    }


    private fun showTipsDialog() {
        val dialogMessage = getString(R.string.tips_camera)
        AlertDialog.Builder(this)
            .setMessage(dialogMessage)
            .setPositiveButton("OK") { _, _ ->

            }
            .show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
