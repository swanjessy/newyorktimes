package com.example.nytimes.presentation.ui.features.headlines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nytimes.R
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.databinding.FragmentNewsBinding
import com.example.nytimes.presentation.ui.BaseFragment
import com.example.nytimes.presentation.ui.features.movies.MoviesReviewAdapter
import com.example.nytimes.presentation.ui.viewmodel.NewsViewModel
import com.example.nytimes.utils.Constants.ARGS_ARTICLE
import com.example.nytimes.utils.Constants.ARGS_MOVIE
import com.example.nytimes.utils.Constants.PARAM_OFFSET
import com.example.nytimes.utils.Constants.PARAM_ORDER
import com.example.nytimes.utils.Constants.PARAM_PERIOD
import com.example.nytimes.utils.Constants.PARAM_TYPE
import com.example.nytimes.utils.Constants.TAG_HEADLINE
import com.example.nytimes.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

@AndroidEntryPoint
class HeadLinesFragment : BaseFragment<FragmentNewsBinding>() {

    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var topStoriesAdapter: TopStoriesAdapter

    @Inject
    lateinit var moviesReviewAdapter: MoviesReviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArticlesRv()
        initMoviesRv()
        observePopularArticles()
        observeTopStories()
        observeMovieReview()

        binding.carousel1.apply {
            registerLifecycle(lifecycle)
        }

        val toolbarTv = binding.includeHeader.tbTitle
        val toolbarImg = binding.includeHeader.bookmarks
        val action = R.id.action_headlines_to_saveFragment
        setUpToolBar(
            toolbarTv, toolbarImg,
            getString(R.string.title_newyork_times),
            View.VISIBLE, action
        )
    }

    /**
     * This method will observe live data for most popular articles to display in carousel
     */
    private fun observePopularArticles() {
        viewModel.getMostPopularNews(PARAM_PERIOD)
        viewModel.popularArticles.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    val listOne = mutableListOf<CarouselItem>()
                    val results = response.data?.results?.take(5)
                    results?.let {
                        for (result in results) {
                            for (media in result.media) {
                                media.media_metadata.let {
                                    val imageUrl = media.media_metadata[2].url
                                    imageUrl.let {
                                        listOne.add(
                                            CarouselItem(
                                                imageUrl = imageUrl,
                                                caption = media.caption,
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    binding.carousel1.setData(listOne)
                }
                is Resource.Error -> {
                    Log.d(TAG_HEADLINE, "observePopularArticles: {${response.message}}")

                }
                is Resource.Loading -> {
                    Log.i(TAG_HEADLINE, "observePopularArticles: Loading....")
                }
                else -> {
                    Log.d(TAG_HEADLINE, getString(R.string.message_popular_response_empty))
                }
            }
        })
    }

    /**
     * Method to bind adapter to Top-Stories recycler view
     * Item OnClick is handled here and sends argument(article) to detail fragment
     */
    private fun initArticlesRv() = with(binding) {
        binding.rvTopStories.apply {
            adapter = topStoriesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            topStoriesAdapter.setOnItemClickListener {
                it?.let {
                    val bundle = Bundle().apply {
                        putParcelable(ARGS_ARTICLE, it)
                    }
                    findNavController().navigate(
                        R.id.action_headlines_to_articleDetailsFragment,
                        bundle
                    )
                }
            }
        }
    }

    /**
     * Method to bind adapter to Movie-critics-review recycler view
     * Item OnClick is handled here and sends argument(movie) to movie-info fragment
     */
    private fun initMoviesRv() = with(binding) {
        rvMovieReviews.apply {
            adapter = moviesReviewAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            moviesReviewAdapter.setOnItemClickListener {
                it?.let {
                    val bundle = Bundle().apply {
                        putParcelable(ARGS_MOVIE, it)
                    }
                    findNavController().navigate(
                        R.id.action_headlines_to_movieInfoFragment,
                        bundle
                    )
                }
            }
        }
    }

    /**
     * This function will observe live data for top-stories and update any changes.
     */
    private fun observeTopStories() {
        viewModel.getTopStories("home")
        viewModel.topStories.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    val results = if (response.data?.results?.size!! > 10) {
                        response.data.results.take(10)
                    } else {
                        response.data.results
                    }
                    val articleList = mutableListOf<Article>()
                    for (result in results) {
                        result.apply {
                            articleList.add(
                                Article(
                                    result.title,
                                    result.abstract,
                                    result.multimedia?.get(1)?.url ?: "",
                                    result.url,
                                    result.updated_date
                                )
                            )
                        }
                    }
                    articleList.sortByDescending { it.updated_date }
                    results.let {
                        topStoriesAdapter.differ.submitList(articleList)
                    }
                    binding.topStoriesPb.visibility = View.GONE
                    binding.rvTopStories.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.topStoriesPb.visibility = View.GONE
                    binding.rvTopStories.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    binding.topStoriesPb.visibility = View.VISIBLE
                    binding.rvTopStories.visibility = View.GONE
                }
                else -> {
                    Log.d(TAG_HEADLINE, getString(R.string.message_trending_response_empty))
                }
            }
        })
    }

    /**
     * This function will observe live data for movie critic reviews and update any changes.
     */
    private fun observeMovieReview() {
        viewModel.getMovieReview(type = PARAM_TYPE, offset = PARAM_OFFSET, order = PARAM_ORDER)
        viewModel.movieReview.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    val results = response.data?.results?.take(10)
                    results?.let {
                        moviesReviewAdapter.differ.submitList(results.toList())
                    }
                    binding.tvMovieCritics.visibility = View.VISIBLE
                    binding.rvMovieReviews.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.tvMovieCritics.visibility = View.GONE
                    binding.rvMovieReviews.visibility = View.GONE

                }
                is Resource.Loading -> {
                    binding.tvMovieCritics.visibility = View.GONE
                    binding.rvMovieReviews.visibility = View.GONE
                }
                else -> {
                    Log.d(TAG_HEADLINE, getString(R.string.message_movie_response_empty))
                }
            }
        })
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewsBinding.inflate(inflater, container, false)
}

