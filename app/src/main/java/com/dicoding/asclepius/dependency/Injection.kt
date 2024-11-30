package com.dicoding.asclepius.dependency

import android.content.Context
import com.dicoding.asclepius.data.retrofit.ApiConfig
import com.dicoding.asclepius.repository.NewsRepository

object Injection {
    fun provideNewsRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }
}
