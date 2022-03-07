package com.example.nytimes.presentation.ui.features.headlines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nytimes.R
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.databinding.FragmentNewsBinding
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
class HeadLinesFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var topStoriesAdapter: TopStoriesAdapter

    @Inject
    lateinit var moviesReviewAdapter: MoviesReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolBar()
        initArticlesRv()
        initMoviesRv()
        observePopularArticles()
        observeTopStories()
        observeMovieReview()

        binding.carousel1.apply {
            registerLifecycle(lifecycle)
        }
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
                    binding.moviesPb.visibility = View.GONE
                    binding.rvMovieReviews.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.moviesPb.visibility = View.GONE
                    binding.rvMovieReviews.visibility = View.VISIBLE

                }
                is Resource.Loading -> {
                    binding.moviesPb.visibility = View.VISIBLE
                    binding.rvMovieReviews.visibility = View.GONE
                }
                else -> {
                    Log.d(TAG_HEADLINE, getString(R.string.message_movie_response_empty))
                }
            }
        })
    }

    /**
     * Method to update common toolbar title.
     */
    private fun setUpToolBar() {
        val headerText = binding.includeHeader.tbTitle
        headerText.text = getString(R.string.title_newyork_times)
        val bookmarkIcon = binding.includeHeader.bookmarks
        bookmarkIcon.setOnClickListener {
            findNavController().navigate(
                R.id.action_headlines_to_saveFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

