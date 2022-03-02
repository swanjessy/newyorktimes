package com.example.nytimes.domain.usecase

import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.domain.repository.NewsRepository
import javax.inject.Inject

class DeleteArticleUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun invoke(article: Article) =
        newsRepository.deleteArticle(article)

}