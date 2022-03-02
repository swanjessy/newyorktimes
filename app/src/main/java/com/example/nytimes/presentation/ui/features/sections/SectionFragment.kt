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
import com.example.nytimes.presentation.viewmodel.NewsViewModel
import com.example.nytimes.utils.DataSet
import com.example.nytimes.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
        observeTopics()
        swipeToRefreshArticles()

    }


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


    private fun initCategoryRv() = with(binding.categoryRv) {
        adapter = categoryAdapter
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        categoryAdapter.differ.submitList(DataSet.category)
        categoryAdapter.setOnItemClickListener {
            viewModel.currentTopic.value = it.title
        }

    }


    private fun swipeToRefreshArticles() = with(binding) {
        refreshArticles.setOnRefreshListener {
            viewModel.refreshTopStories {
                refreshArticles.isRefreshing = true
            }
        }
    }

    private fun observeTopics() {
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
                    Log.i("TAG", "observeTopics: status = ${response.message}")
                    Toast.makeText(
                        requireContext(),
                        "Some error occurred, you have reached quota limit. Please try again later!",
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