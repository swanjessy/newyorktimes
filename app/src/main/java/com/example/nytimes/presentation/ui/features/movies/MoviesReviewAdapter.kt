package com.example.nytimes.presentation.ui.features.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nytimes.data.model.moviesreview.Result
import com.example.nytimes.databinding.ItemMovieReviewBinding

class MoviesReviewAdapter : RecyclerView.Adapter<MoviesReviewAdapter.ViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.display_title == newItem.display_title
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieReviewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(
        val binding: ItemMovieReviewBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.tvDisplayTitle.text = result.display_title
            binding.tvRating.text = "MPAA Rating : ${result.mpaa_rating}"

            val imageUrl = result.multimedia.src
            imageUrl?.let {
                Glide.with(binding.imgMovie.context).load(imageUrl)
                    .into(binding.imgMovie)
            }

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(result)
                }
            }
        }
    }

    private var onItemClickListener: ((Result?) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result?) -> Unit) {
        onItemClickListener = listener
    }


}









