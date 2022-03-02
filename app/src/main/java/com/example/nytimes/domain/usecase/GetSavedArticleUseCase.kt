package com.example.nytimes.domain.usecase

import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedArticleUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun invoke(): Flow<List<Article>> {
        return newsRepository.getSavedArticle()
    }
}