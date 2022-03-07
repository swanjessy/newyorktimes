package com.example.nytimes.presentation.ui.features.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.nytimes.R
import com.example.nytimes.databinding.FragmentArticleDetailsBinding
import com.example.nytimes.presentation.ui.BaseFragment
import com.example.nytimes.presentation.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailsFragment : BaseFragment<FragmentArticleDetailsBinding>() {

    private val args: ArticleDetailsFragmentArgs by navArgs()
    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                toast(getString(R.string.title_article_saved))
            }
        }

        val toolbarTv = binding.includeHeader.tbTitle
        val toolbarImg = binding.includeHeader.bookmarks
        val action = R.id.action_articleDetailsFragment_to_saveFragment
        setUpToolBar(
            toolbarTv, toolbarImg,
            getString(R.string.title_article_detail),
            View.VISIBLE, action
        )
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentArticleDetailsBinding.inflate(inflater, container, false)
}