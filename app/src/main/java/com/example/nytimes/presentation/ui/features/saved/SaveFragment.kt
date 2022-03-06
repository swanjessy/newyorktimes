package com.example.nytimes.presentation.ui.features.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nytimes.R
import com.example.nytimes.databinding.FragmentSaveBinding
import com.example.nytimes.presentation.ui.features.sections.SectionAdapter
import com.example.nytimes.presentation.ui.viewmodel.NewsViewModel
import com.example.nytimes.utils.Constants.ARGS_ARTICLE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SaveFragment : Fragment() {

    private var _binding: FragmentSaveBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var sectionAdapter: SectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        initArticlesRv()
        viewModel.getSavedNews().observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                binding.tvEmptyList.visibility = View.VISIBLE
                binding.articleRv.visibility = View.GONE
            } else {
                sectionAdapter.differ.submitList(it)
                binding.tvEmptyList.visibility = View.GONE
                binding.articleRv.visibility = View.VISIBLE
            }
        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val article = sectionAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Deleted Successfully", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            viewModel.saveArticle(article)
                        }
                        show()
                    }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.articleRv)
        }
    }

    private fun initArticlesRv() = with(binding) {
        articleRv.apply {
            adapter = sectionAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            sectionAdapter.setOnItemClickListener {
                it?.let {
                    val bundle = Bundle().apply {
                        putParcelable(ARGS_ARTICLE, it)
                    }
                    findNavController().navigate(
                        R.id.action_saveFragment_to_articleDetailsFragment,
                        bundle
                    )
                }
            }
        }
    }

    private fun setUpToolbar() {
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolBar)
        val title = toolbar?.findViewById<TextView>(R.id.tb_title)
        title?.text = getString(R.string.title_saved_articles)
    }
}