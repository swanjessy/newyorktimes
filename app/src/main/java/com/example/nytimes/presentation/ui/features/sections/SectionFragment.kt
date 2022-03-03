package com.example.nytimes.presentation.ui.features.sections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nytimes.R
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.databinding.FragmentSectionsBinding
import com.example.nytimes.presentation.ui.viewmodel.NewsViewModel
import com.example.nytimes.utils.DataSet
import com.example.nytimes.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "Section"

@AndroidEntryPoint
class SectionFragment : Fragment() {

    private var _binding: FragmentSectionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    @Inject
    lateinit var sectionAdapter: SectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSectionsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        initArticlesRv()
        initCategoryRv()
        observeCategoryAndUpdateArticles()
        swipeToRefreshArticles()

    }

    /**
     * Method to bind adapter to Top-Stories recycler view
     * Item OnClick is handled here and sends argument(article) to detail fragment
     */
    private fun initArticlesRv() = with(binding) {
        articleRv.apply {
            adapter = sectionAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            sectionAdapter.setOnItemClickListener {
                it?.let {
                    val bundle = Bundle().apply {
                        putParcelable("article", it)
                    }
                    findNavController().navigate(
                        R.id.action_sections_to_articleDetailsFragment,
                        bundle
                    )
                }
            }
        }
    }

    /**
     * Method to bind adapter to Category recycler view
     * Updates the selected category on Item OnClick
     */
    private fun initCategoryRv() = with(binding.categoryRv) {
        adapter = categoryAdapter
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter.differ.submitList(DataSet.category)
        categoryAdapter.setOnItemClickListener {
            viewModel.currentTopic.value = it.title
        }
    }

    /**
     * Method to swipe and refresh articles displayed
     */
    private fun swipeToRefreshArticles() = with(binding) {
        refreshArticles.setOnRefreshListener {
            viewModel.refreshTopStories {
                refreshArticles.isRefreshing = true
            }
        }
    }

    /**
     * This method will update the articles to display when the selected category changes.
     */
    private fun observeCategoryAndUpdateArticles() {
        if (viewModel.currentTopic.value.isNullOrEmpty()) {
            viewModel.currentTopic.value = DataSet.category[0].title
        }
        viewModel.currentTopic.observe(viewLifecycleOwner) {
            if (viewModel.networkObserver.value == true) {
                binding.refreshArticles.isRefreshing = true
            }
            viewModel.getTopStories(it.toString())
        }
        viewModel.topStories.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    binding.refreshArticles.isRefreshing = false
                    val results = if (response.data?.results?.size!! > 10) {
                        response.data.results.take(10)
                    } else {
                        response.data.results
                    }
                    val articleList = mutableListOf<Article>()
                    for (result in results) {
                        val imageURL = result.multimedia?.get(1)?.url ?: ""
                        articleList.add(
                            Article(
                                result.title,
                                result.abstract,
                                imageURL,
                                result.url,
                                result.updated_date
                            )
                        )
                    }
                    articleList.sortByDescending { it.updated_date }
                    sectionAdapter.differ.submitList(articleList)

                }
                is Resource.Error -> {
                    Log.i(TAG, "observeTopics: status = ${response.message}")
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_message_reached_quota_limit),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.refreshArticles.isRefreshing = false
                }
                is Resource.Loading -> {
                    binding.refreshArticles.isRefreshing = false
                }
            }

        })
    }

    /**
     * Update common toolbar title.
     */
    private fun setUpToolbar() {
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolBar)
        val title = toolbar?.findViewById<TextView>(R.id.tb_title)
        title?.text = getString(R.string.title_trending_stories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}