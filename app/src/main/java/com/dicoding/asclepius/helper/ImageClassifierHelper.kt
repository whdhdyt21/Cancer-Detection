package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.dicoding.asclepius.R
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private var threshold: Float = DEFAULT_THRESHOLD,
    private var maxResults: Int = DEFAULT_MAX_RESULTS,
    private val modelName: String = DEFAULT_MODEL_NAME,
    private val context: Context,
    private val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        try {
            val options = ImageClassifier.ImageClassifierOptions.builder()
                .setScoreThreshold(threshold)
                .setMaxResults(maxResults)
                .apply {
                    setBaseOptions(BaseOptions.builder().setNumThreads(NUM_THREADS).build())
                }
                .build()

            imageClassifier = ImageClassifier.createFromFileAndOptions(context, modelName, options)
        } catch (e: IllegalStateException) {
            val errorMessage = context.getString(R.string.image_classifier_failed)
            classifierListener?.onError(errorMessage)
            Log.e(TAG, e.message.orEmpty())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        try {
            val bitmap = loadImageAsBitmap(imageUri)?.copy(Bitmap.Config.ARGB_8888, true)
            bitmap?.let { processImage(it) }
        } catch (e: Exception) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.orEmpty())
        }
    }

    private fun loadImageAsBitmap(imageUri: Uri): Bitmap? {
        val contentResolver = context.contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        }
    }

    private fun processImage(bitmap: Bitmap) {
        val imageProcessor = ImageProcessor.Builder().build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
        val inferenceStartTime = SystemClock.uptimeMillis()
        val results = imageClassifier?.classify(tensorImage)
        val inferenceTime = SystemClock.uptimeMillis() - inferenceStartTime
        classifierListener?.onResults(results, inferenceTime)
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: List<Classifications>?, inferenceTime: Long)
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
        private const val DEFAULT_THRESHOLD = 0.1f
        private const val DEFAULT_MAX_RESULTS = 1
        private const val DEFAULT_MODEL_NAME = "cancer_classification.tflite"
        private const val NUM_THREADS = 4
    }
}