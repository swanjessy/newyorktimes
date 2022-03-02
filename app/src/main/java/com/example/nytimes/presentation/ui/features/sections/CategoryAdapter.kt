package com.example.nytimes.presentation.ui.features.sections

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nytimes.R
import com.example.nytimes.data.model.topstories.Category
import com.example.nytimes.databinding.ItemPostCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectedItem: Int = 0

    private val callback = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPostCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.bind(category)
    }

    inner class ViewHolder(val binding: ItemPostCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.apply {

                itemCategoryTitle.text = category.title

                binding.root.setOnClickListener {
                    onItemClickListener?.let {
                        it(category)
                    }
                    if (selectedItem == absoluteAdapterPosition) {
                        notifyItemChanged(absoluteAdapterPosition)
                        return@setOnClickListener
                    }

                    selectedItem = absoluteAdapterPosition
                    notifyDataSetChanged()
                }

                // if item selected then change it's state color
                when (selectedItem) {
                    absoluteAdapterPosition -> {
                        itemCategoryTitle.setTextColor(
                            ContextCompat.getColor(
                                itemCategoryTitle.context,
                                R.color.white
                            )
                        )

                        MyDrawableCompat.setColorFilter(
                            itemCategoryTitle.background,
                            ContextCompat.getColor(
                                root.context,
                                R.color.flat_grey_dark_1
                            )
                        )
                    }
                    else -> {
                        itemCategoryTitle.setTextColor(
                            ContextCompat.getColor(
                                itemCategoryTitle.context,
                                R.color.black
                            )
                        )
                        MyDrawableCompat.setColorFilter(
                            itemCategoryTitle.background,
                            ContextCompat.getColor(root.context, R.color.flat_white_light)
                        )
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((Category) -> Unit)? = null
    fun setOnItemClickListener(listener: (Category) -> Unit) {
        onItemClickListener = listener
    }

    object MyDrawableCompat {
        fun setColorFilter(drawable: Drawable, color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
            } else {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }
}
