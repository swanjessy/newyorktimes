package com.example.nytimes.presentation.ui.features.movies

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.nytimes.R
import com.example.nytimes.databinding.FragmentMovieInfoBinding

class MovieInfoFragment : Fragment() {

    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!
    private val args: MovieInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = args.movie
        val reviewURL = bundle.link.url
        val imageUrl = bundle.multimedia.src

        setUpToolBar()

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
    }

    private fun setUpToolBar() {
        val headerText = binding.includeHeader.tbTitle
        headerText.text = getString(R.string.title_movie_review)
        val bookmarkIcon = binding.includeHeader.bookmarks
        bookmarkIcon.setOnClickListener {
            findNavController().navigate(
                R.id.action_movieInfoFragment_to_saveFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}