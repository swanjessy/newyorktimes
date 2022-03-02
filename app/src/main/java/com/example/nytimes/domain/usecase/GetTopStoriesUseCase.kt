package com.example.nytimes.domain.usecase

import com.example.nytimes.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopStoriesUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun invoke(section: String) =
        newsRepository.getTopStories(section)

}