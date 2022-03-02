package com.example.nytimes.domain.usecase

import com.example.nytimes.domain.repository.NewsRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend fun invoke(type: String, offset: Int, order: String) =
        newsRepository.getMovieReview(type, offset, order)

}