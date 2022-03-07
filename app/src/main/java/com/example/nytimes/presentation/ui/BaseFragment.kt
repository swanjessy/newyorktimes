package com.example.nytimes.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    fun toast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Method to update common toolbar.
     */
    fun setUpToolBar(
        tv: TextView,
        img: ImageView,
        toolbarText: String,
        toolbarIconVisibility: Int,
        @IdRes resId: Int? = null
    ) {
        tv.text = toolbarText
        img.visibility = toolbarIconVisibility
        img.setOnClickListener {
            resId?.let { action -> findNavController().navigate(action) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}