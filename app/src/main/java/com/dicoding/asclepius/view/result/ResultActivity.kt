package com.dicoding.asclepius.view.result

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.ClassificationEntity
import com.dicoding.asclepius.data.local.entity.ClassificationResult
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.ViewModelFactory
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var result: ClassificationResult

    private val timeStamp: String by lazy {
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale("id", "ID")).format(Date())
    }

    private val classificationViewModel: ClassificationViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance(application))[ClassificationViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        result = getResultFromIntent(savedInstanceState)

        displayResult()
        binding.btnSave.setOnClickListener { saveToHistory() }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.result)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun getResultFromIntent(savedInstanceState: Bundle?): ClassificationResult {
        return savedInstanceState?.getParcelable(EXTRA_RESULT)
            ?: intent.getParcelableExtra(EXTRA_RESULT)
            ?: throw IllegalStateException("Result not found!")
    }

    private fun displayResult() {
        binding.resultImage.setImageURI(result.imageUri)
        binding.resultText.text = buildString {
            append(result.category)
            append(" - ")
            append(result.confidence)
        }
    }

    private fun saveToHistory() {
        if (!binding.btnSave.isEnabled) return

        val fileName = "${timeStamp}.jpg"
        saveImageToFile(fileName)

        val imageUri = Uri.fromFile(File(filesDir, fileName))
        classificationViewModel.insert(
            ClassificationEntity(
                category = result.category,
                confidence = result.confidence,
                imageUri = imageUri.toString()
            )
        )

        updateSaveButton()
        Toast.makeText(this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT).show()
    }

    private fun saveImageToFile(fileName: String) {
        this.openFileOutput(fileName, MODE_PRIVATE).use { output ->
            val inputStream = contentResolver.openInputStream(result.imageUri) as InputStream
            val buffer = ByteArray(4 * 1024)
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                output.write(buffer, 0, read)
            }
            inputStream.close()
        }
    }

    private fun updateSaveButton() {
        binding.btnSave.apply {
            isEnabled = false
            text = getString(R.string.saved)
            setBackgroundResource(R.drawable.button_filled_background)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_RESULT, result)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }
}
