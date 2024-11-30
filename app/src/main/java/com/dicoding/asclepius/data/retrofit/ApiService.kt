package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.response.NewsResponse

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("v2/top-headlines?q=cancer&category=health&language=en&apiKey=${BuildConfig.NEWS_API_KEY}")
    fun getNews(): Call<NewsResponse>
}