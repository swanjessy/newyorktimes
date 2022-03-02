package com.example.nytimes.presentation.ui.features.headlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.databinding.ItemTopStoriesBinding

class TopStoriesAdapter : RecyclerView.Adapter<TopStoriesAdapter.ViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopStoriesBinding
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
        val binding: ItemTopStoriesBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Article?) {
            binding.tvTitle.text = result?.title

            result?.imageUrl?.let {
                Glide.with(binding.imageView.context).load(it)
                    .into(binding.imageView)

            }

            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(result)
                }
            }
        }
    }

    private var onItemClickListener: ((Article?) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article?) -> Unit) {
        onItemClickListener = listener
    }


}









