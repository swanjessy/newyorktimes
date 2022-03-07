package com.example.nytimes.presentation.ui.features.movies

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.nytimes.R
import com.example.nytimes.databinding.FragmentMovieInfoBinding
import com.example.nytimes.presentation.ui.BaseFragment

class MovieInfoFragment : BaseFragment<FragmentMovieInfoBinding>() {

    private val args: MovieInfoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = args.movie
        val reviewURL = bundle.link.url
        val imageUrl = bundle.multimedia.src

        binding.titleMovie.text = bundle.headline
        binding.tvDisplayTitle.text = bundle.display_title
        binding.tvByline.text = bundle.byline
        binding.abstractMovie.text = bundle.summary_short
        binding.tvOpeningDate.text =
            String.format(resources.getString(R.string.title_opening_date), bundle.opening_date)

        Glide.with(binding.imgMovie.context).load(imageUrl)
            .into(binding.imgMovie)

        binding.tvHyperlink.apply {
            movementMethod = LinkMovementMethod.getInstance()
            setLinkTextColor(Color.BLUE)
            text = bundle.link.suggested_link_text
            setOnClickListener {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(reviewURL))
                startActivity(browserIntent)
            }
        }

        val toolbarTv = binding.includeHeader.tbTitle
        val toolbarImg = binding.includeHeader.bookmarks
        val action = R.id.action_movieInfoFragment_to_saveFragment
        setUpToolBar(
            toolbarTv, toolbarImg,
            getString(R.string.title_movie_review),
            View.VISIBLE, action
        )
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMovieInfoBinding.inflate(inflater, container, false)
}