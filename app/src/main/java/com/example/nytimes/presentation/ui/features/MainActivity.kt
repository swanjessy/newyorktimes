package com.example.nytimes.presentation.ui.features

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nytimes.R
import com.example.nytimes.databinding.ActivityMainBinding
import com.example.nytimes.utils.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeNetworkConnectivity()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.navBottom.setupWithNavController(navController)
    }

    private fun observeNetworkConnectivity() {
        NetworkUtils.observeConnectivity(this)
            .observe(this) { isConnected ->
                if (isConnected) {
                    Toast.makeText(this, "You're back Online", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}