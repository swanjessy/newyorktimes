package com.example.nytimes.domain.usecase

import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.domain.repository.NewsRepository
import javax.inject.Inject

class SaveArticleUseCase @Inject constructor(private val newsRepository: NewsRepository) {

    suspend fun invoke(article: Article) =
        newsRepository.saveArticle(article)
}