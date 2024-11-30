package com.dicoding.asclepius.view.analyze

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.local.entity.ClassificationResult
import com.dicoding.asclepius.databinding.FragmentAnalyzeBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class AnalyzeFragment : Fragment() {
    private lateinit var analyzeViewModel: AnalyzeViewModel
    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { startCropActivity(it) } ?: logMessage("No media selected")
    }

    private val cropActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                UCrop.getOutput(result.data!!)?.let {
                    analyzeViewModel.setCurrentImageUri(it)
                } ?: showToast("Crop failed")
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                showToast("Crop error: ${UCrop.getError(result.data!!)?.message}")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        analyzeViewModel = ViewModelProvider(this)[AnalyzeViewModel::class.java]

        setupUI()
        observeViewModel()
        return binding.root
    }

    private fun setupUI() {
        binding.galleryButton.setOnClickListener { openGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
    }

    private fun observeViewModel() {
        analyzeViewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.previewImageView.setImageURI(uri)
            }
        }
    }

    private fun openGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCropActivity(uri: Uri) {
        val destinationUri = Uri.fromFile(
            requireContext().cacheDir.resolve("${System.currentTimeMillis()}.jpg")
        )
        val cropIntent = UCrop.of(uri, destinationUri).getIntent(requireContext())
        cropActivityLauncher.launch(cropIntent)
    }

    private fun analyzeImage() {
        val currentImageUri = analyzeViewModel.currentImageUri.value
        if (currentImageUri == null) {
            showToast("No image selected")
            return
        }

        imageClassifierHelper = ImageClassifierHelper(
            context = requireContext(),
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    binding.progressIndicator.visibility = View.GONE
                    showToast(error)
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    binding.progressIndicator.visibility = View.GONE
                    processClassificationResults(results)
                }
            }
        )

        binding.progressIndicator.visibility = View.VISIBLE
        imageClassifierHelper.classifyStaticImage(currentImageUri)
    }

    private fun moveToResult(result: ClassificationResult) {
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_RESULT, result)
        }
        startActivity(intent)
    }

    private fun processClassificationResults(results: List<Classifications>?) {
        results?.firstOrNull()?.categories?.firstOrNull()?.let { category ->
            val label = category.label.toString()
            val confidence = NumberFormat.getPercentInstance().format(category.score)
            val imageUri = analyzeViewModel.currentImageUri.value ?: run {
                showToast("Image URI not found")
                return
            }

            val classificationResult = ClassificationResult(
                category = label,
                confidence = confidence,
                imageUri = imageUri
            )

            moveToResult(classificationResult)
        } ?: showToast("No prediction result")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun logMessage(message: String) {
        Log.d("AnalyzeFragment", message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
