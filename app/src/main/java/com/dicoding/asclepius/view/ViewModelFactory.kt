package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.view.news.NewsViewModel
import com.dicoding.asclepius.view.result.ClassificationViewModel
import com.dicoding.asclepius.repository.NewsRepository

class ViewModelFactory private constructor(
    private val application: Application,
    private val newsRepository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    application,
                    com.dicoding.asclepius.dependency.Injection.provideNewsRepository(application)
                ).also { INSTANCE = it }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ClassificationViewModel::class.java) -> ClassificationViewModel(application) as T
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> NewsViewModel(newsRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
