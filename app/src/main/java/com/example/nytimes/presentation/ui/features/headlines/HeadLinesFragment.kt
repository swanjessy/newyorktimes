package com.example.nytimes.presentation.ui.features.headlines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nytimes.R
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.databinding.FragmentNewsBinding
import com.example.nytimes.presentation.ui.features.movies.MoviesReviewAdapter
import com.example.nytimes.presentation.viewmodel.NewsViewModel
import com.example.nytimes.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import javax.inject.Inject

private const val TAG = "Headlines"

@AndroidEntryPoint
class HeadLinesFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private var period = 1

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

        setUpToolbar()
        initArticlesRv()
        initMoviesRv()
        observePopularArticles()
        observeTopics()
        observeMovieReview()

        binding.carousel1.apply {
            registerLifecycle(lifecycle)
        }

    }


    private fun observePopularArticles() {
        viewModel.getMostPopularNews(period)
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
                    Log.i(TAG, "observePopularArticles: {${response.message}}")

                }
                is Resource.Loading -> {
                    Log.i(TAG, "observePopularArticles: Loading....")
                }
            }

        })
    }


    private fun initArticlesRv() = with(binding) {
        binding.rvTopStories.apply {
            adapter = topStoriesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            topStoriesAdapter.setOnItemClickListener {
                it?.let {
                    val bundle = Bundle().apply {
                        putParcelable("article", it)
                    }
                    findNavController().navigate(
                        R.id.action_headlines_to_articleDetailsFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun initMoviesRv() = with(binding) {
        binding.rvMovieReviews.apply {
            adapter = moviesReviewAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            moviesReviewAdapter.setOnItemClickListener {
                it?.let {
                    val bundle = Bundle().apply {
                        putParcelable("movie", it)
                    }
                    findNavController().navigate(
                        R.id.action_headlines_to_movieInfoFragment,
                        bundle
                    )
                }
            }
        }
    }


    private fun observeTopics() {
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
                    binding.rvTopStories.visibility = View.VISIBLE
                    binding.topStoriesPb.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.topStoriesPb.visibility = View.GONE
                    binding.rvTopStories.visibility = View.VISIBLE
                }
                is Resource.Loading -> {
                    binding.topStoriesPb.visibility = View.VISIBLE
                    binding.rvTopStories.visibility = View.GONE
                }
            }

        })
    }

    private fun observeMovieReview() {
        viewModel.getMovieReview(type = "picks", offset = 10, order = "by-opening-date")
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
                    Log.i(TAG, "observeMovieReview: ")
                    binding.rvMovieReviews.visibility = View.VISIBLE

                }
                is Resource.Loading -> {
                    binding.moviesPb.visibility = View.VISIBLE
                    binding.rvMovieReviews.visibility = View.GONE
                }
            }

        })
    }

    private fun setUpToolbar() {
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolBar)
        val title = toolbar?.findViewById<TextView>(R.id.tb_title)
        title?.text = getString(R.string.title_newyork_times)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

