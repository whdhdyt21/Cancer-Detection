package com.dicoding.asclepius.view.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.repository.NewsRepository
import com.dicoding.asclepius.utils.Result

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private lateinit var _news: LiveData<Result<List<ArticlesItem>>>
    val news: LiveData<Result<List<ArticlesItem>>> get() = _news

    fun getNewsData(): LiveData<Result<List<ArticlesItem>>> {
        _news = newsRepository.getNews()
        return _news
    }
}