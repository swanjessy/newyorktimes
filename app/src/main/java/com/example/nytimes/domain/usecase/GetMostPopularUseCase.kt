package com.example.nytimes.domain.usecase

import com.example.nytimes.domain.repository.NewsRepository
import javax.inject.Inject

class GetMostPopularUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun invoke(period: Int) = newsRepository.getMostPopularNews(period)

}