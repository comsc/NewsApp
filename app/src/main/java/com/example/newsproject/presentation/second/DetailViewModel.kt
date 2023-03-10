package com.example.newsproject.presentation.second

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsproject.data.DataObject
import com.example.newsproject.data.models.Article
import com.example.newsproject.utils.extensions.asLiveData
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {

    private val localRepository = DataObject.localRepository

    private val _detailArticle = MutableLiveData<Article>()
    val detailArticle = _detailArticle.asLiveData()

    fun setDetail(article: Article) {
        _detailArticle.value = article
    }

    fun handleFavorite() {
        val article = _detailArticle.value ?: return
        if (article.isFavorite) {
            deleteFavorite(article)
        } else {
            addToFavorite(article)
        }
    }
    private fun addToFavorite(article: Article) {
        val actualArticle = article.copy(isFavorite = true)
        setDetail(actualArticle)
        viewModelScope.launch {
            localRepository.addArticle(actualArticle)
        }
    }

    private fun deleteFavorite(article: Article) {
        val actualArticle = article.copy(isFavorite = false)
        setDetail(actualArticle)
        viewModelScope.launch {
            localRepository.removeArticle(article.copy(isFavorite = true))
        }
    }
}