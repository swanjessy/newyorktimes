package com.example.nytimes.presentation.ui.features.sections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nytimes.data.model.topstories.Article
import com.example.nytimes.databinding.ItemStorySectionBinding

class SectionAdapter : RecyclerView.Adapter<SectionAdapter.ViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.updated_date == newItem.updated_date
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStorySectionBinding
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
        val binding: ItemStorySectionBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Article?) {
            binding.tvTitle.text = result?.title
            binding.tvHeadlines.text = result?.description
            result?.imageUrl?.let {
                Glide.with(binding.imageView.context).load(it)
                    .into(binding.imageView)

            }
            binding.root.setOnClickListener {
                onItemClickListener?.let {
                    if (result != null) {
                        it(result)
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((Article?) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article?) -> Unit) {
        onItemClickListener = listener
    }
}









