package com.example.nytimes.presentation.ui.viewmodel

import androidx.lifecycle.*
import com.example.nytimes.data.model.mostpopular.MostPopular
import com.example.nytimes.data.model.moviesreview.MovieReview
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.data.model.topstories.TopStories
import com.example.nytimes.domain.usecase.*
import com.example.nytimes.utils.NetworkManager
import com.example.nytimes.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getMostPopularUseCase: GetMostPopularUseCase,
    private val getTopStoriesUseCase: GetTopStoriesUseCase,
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getSavedArticleUseCase: GetSavedArticleUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    private val networkManager: NetworkManager
) : ViewModel() {

    val networkObserver = networkManager.observeConnectionStatus

    private val _popularArticles: MutableLiveData<Resource<MostPopular>> = MutableLiveData()
    val popularArticles: LiveData<Resource<MostPopular>>
        get() = _popularArticles

    private val _topStories: MutableLiveData<Resource<TopStories>> = MutableLiveData()
    val topStories: LiveData<Resource<TopStories>>
        get() = _topStories

    val currentTopic: MutableLiveData<String> by lazy {
        MutableLiveData<String>().defaultTopic("")
    }

    private val _movieReview: MutableLiveData<Resource<MovieReview>> = MutableLiveData()
    val movieReview: LiveData<Resource<MovieReview>>
        get() = _movieReview


    fun getMostPopularNews(period: Int) = viewModelScope.launch {
        if (networkObserver.value == true) {
            getMostPopularUseCase.invoke(period)
                .collect {
                    _popularArticles.postValue(it)
                }
        }
    }

    fun getTopStories(section: String) = viewModelScope.launch {
        if (networkObserver.value == true) {
            getTopStoriesUseCase.invoke(section)
                .collect {
                    _topStories.postValue(it)
                }
        }
    }

    fun refreshTopStories(refreshFailed: () -> Unit = {}) = viewModelScope.launch {
        if (networkObserver.value == true) {
            getTopStoriesUseCase.invoke(currentTopic.value!!)
                .collect {
                    _topStories.postValue(it)
                }
        } else {
            refreshFailed.invoke()
        }
    }

    fun getMovieReview(type: String, offset: Int, order: String) = viewModelScope.launch {
        getMoviesUseCase.invoke(type, offset, order)
            .collect {
                _movieReview.postValue(it)
            }

    }


    //local data
    fun saveArticle(article: Article) = viewModelScope.launch {
        saveArticleUseCase.invoke(article)
    }

    fun getSavedNews() = liveData {
        getSavedArticleUseCase.invoke().collect {
            emit(it)
        }
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        deleteArticleUseCase.invoke(article)
    }


    // set default topic when opening
    private fun <T : Any?> MutableLiveData<T>.defaultTopic(initialValue: T) =
        apply { setValue(initialValue) }


}
