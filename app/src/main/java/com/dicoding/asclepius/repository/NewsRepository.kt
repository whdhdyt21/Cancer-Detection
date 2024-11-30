package com.dicoding.asclepius.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.NewsResponse
import com.dicoding.asclepius.data.retrofit.ApiService
import com.dicoding.asclepius.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository private constructor(private val apiService: ApiService) {

    private val _result = MediatorLiveData<Result<List<ArticlesItem>>>()

    fun getNews(): LiveData<Result<List<ArticlesItem>>> {
        _result.value = Result.Loading
        val client = apiService.getNews()
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.articles?.let { articles ->
                        _result.value = Result.Success(articles)
                    } ?: run {
                        _result.value = Result.Error("Response body is null")
                    }
                } else {
                    _result.value = Result.Error("Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _result.value = Result.Error("Error: ${t.message}")
            }
        })
        return _result
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null

        fun getInstance(apiService: ApiService): NewsRepository {
            return instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService).also { instance = it }
            }
        }
    }
}
