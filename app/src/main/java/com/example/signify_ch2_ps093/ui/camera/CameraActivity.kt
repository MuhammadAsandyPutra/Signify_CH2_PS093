package com.example.signify_ch2_ps093.ui.camera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.signify_ch2_ps093.R
import com.example.signify_ch2_ps093.data.PeragakanAnswer
import com.example.signify_ch2_ps093.databinding.ActivityCameraBinding
import com.example.signify_ch2_ps093.ml.TfliteModelling
import com.example.signify_ch2_ps093.ui.quiz.QuizResultFragment
import com.example.signify_ch2_ps093.ui.utils.Constant.SESSION
import com.example.signify_ch2_ps093.ui.utils.hide
import com.example.signify_ch2_ps093.ui.utils.show
import com.google.gson.Gson
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.SimpleDateFormat
import java.util.Locale

class CameraActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var currentImageUri: Uri? = null
    val peragakanAnswer = mutableListOf<PeragakanAnswer>()

    private val arrayOfWords: Array<String> = arrayOf(
        "1", "10", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "Ada", "Aku", "Apa", "Ayah",
        "B", "Baik", "Bantu", "Benci", "Bercanda", "Bisa",
        "C", "Cinta",
        "D", "Dia", "Dimana",
        "E",
        "F",
        "G",
        "H", "Halo",
        "I", "Ingin", "Ini",
        "J", "Jalan-jalan", "Jam", "Jangan",
        "K", "Kakak", "Kamu", "Kangen", "Kapan", "Keadaan", "Kenapa", "Keren", "Kerja",
        "L", "Lihat",
        "M", "Maaf", "Main", "Malam", "Malas", "Malu", "Marah", "Minta", "Minum",
        "N",
        "O",
        "P", "Pagi",
        "Q",
        "R", "Rumah",
        "S", "Sabar", "Sakit", "Salah", "Sama-sama", "Sayang", "Sedih", "Sekolah", "Semangat", "Senang", "Siang", "Siapa", "Suka",
        "T", "Terima kasih", "Tolong",
        "U",
        "V",
        "W",
        "X",
        "Y",
        "Z"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        viewBinding.captureImage.setOnClickListener {
            startCapturingImage()
        }

        viewBinding.btnGaleri.setOnClickListener {
            openGallery()
        }

        viewBinding.btnTips.setOnClickListener {
            showTipsDialog()
        }

        viewBinding.btnLanjutkan.setOnClickListener {

            //proses gambar ke model
            currentImageUri?.let { uri ->
                processCapturedImage(uri)
            }

            val fragment = QuizResultFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()


        }


    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            // ImageCapture
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            // Select front camera as a default
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun startCapturingImage() {
        val imageCapture = this.imageCapture ?: return
        viewBinding.captureImage.isEnabled = false

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: return
                    currentImageUri = savedUri

                    showImagePreview(savedUri)
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun processCapturedImage(imageUri: Uri) {
       val model = TfliteModelling.newInstance(this)

        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false)
            val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)

            // Load the ByteBuffer into a TensorBuffer
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(byteBuffer)

            // Run inference using the TFLite model
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            model.close()

            // Get the result
            val result = getClassificationResult(outputFeature0.floatArray, arrayOfWords)
            Log.d(TAG, "Prediction result: $result")
            Toast.makeText(this, "Hasil Predict : $result", Toast.LENGTH_SHORT).show()

            inputStream?.close()

            val answer = PeragakanAnswer(result)
            peragakanAnswer.add(answer)

            UserPreferences.savePeragakanAnswer(this, answer)


        } catch (e: Exception) {
            Log.e(TAG, "Error processing image: ${e.message}")
        }
    }


    private fun getClassificationResult(arr: FloatArray, data: Array<String>): String {
        var maxPos = 0
        var maxConfidence = 0.0f

        for (i in arr.indices){
            if (arr[i]> maxConfidence){
                maxConfidence = arr[i]
                maxPos = i
            }
        }
        return data[maxPos]
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(1 * 224 * 224 * 3 * java.lang.Float.SIZE / java.lang.Byte.SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(224 * 224)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until 224) {
            for (j in 0 until 224) {
                val input = intValues[pixel++]
                byteBuffer.putFloat(((input shr 16 and 0xFF) - 0) / 255.0f)
                byteBuffer.putFloat(((input shr 8 and 0xFF) - 0) / 255.0f)
                byteBuffer.putFloat(((input and 0xFF) - 0) / 255.0f)
            }
        }
        return byteBuffer
    }



    private fun showImagePreview(imageUri: Uri) {
        currentImageUri = imageUri
        viewBinding.viewFinder.hide()
        viewBinding.cvPreviewImage.show()
        viewBinding.btnLanjutkan.show()
        viewBinding.captureImage.hide()
        viewBinding.btnGaleri.hide()
        viewBinding.btnTips.hide()
        viewBinding.ivCorrect.hide()
        viewBinding.tvAnswerCorrect.hide()

        initializeImagePreview(imageUri)
    }

    private fun initializeImagePreview(imageUri: Uri) {
        viewBinding.imageView.setImageURI(imageUri)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Image Picker", "No media selected")
        }
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image  URI", "showVideo: $it")
            showImagePreview(it)
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
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
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


    object UserPreferences {
        // Fungsi yang sebelumnya ada

        private const val PERAGAKAN_ANSWER_KEY = "peragakan_answer"

        fun savePeragakanAnswer(context: Context, answer: PeragakanAnswer) {
            val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val gson = Gson()
            val json = gson.toJson(answer)
            editor.putString(PERAGAKAN_ANSWER_KEY, json)
            editor.apply()
        }

        fun getPeragakanAnswer(context: Context): PeragakanAnswer? {
            val sharedPref = context.getSharedPreferences(SESSION, Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPref.getString(PERAGAKAN_ANSWER_KEY, null)
            return gson.fromJson(json, PeragakanAnswer::class.java)
        }
    }


    companion object {
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10

        private val REQUIRED_PERMISSIONS = mutableListOf(Manifest.permission.CAMERA).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}
