package com.example.nytimes.presentation.ui.features.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.nytimes.R
import com.example.nytimes.databinding.FragmentArticleDetailsBinding
import com.example.nytimes.presentation.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ArticleDetailsFragment : Fragment() {

    private var _binding: FragmentArticleDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ArticleDetailsFragmentArgs by navArgs()
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        val bundle = args.article
        val articleURL = bundle.url

        binding.webView.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                webViewClient = webViewClient
            }
            loadUrl(articleURL)
        }

        binding.btnSaveArticle.setOnClickListener {
            viewModel.saveArticle(bundle).also {
                Toast.makeText(requireContext(), "Article saved successfully", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    private fun setupToolbar() {
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolBar)
        val title = toolbar?.findViewById<TextView>(R.id.tb_title)
        title?.text = getString(R.string.title_article_detail)
    }

}