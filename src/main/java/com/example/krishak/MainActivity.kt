package com.example.krishak

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krishak.databinding.ActivityMainBinding
import com.example.krishak.viewmodel.CropViewModel
import com.example.krishak.viewmodel.CropViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CropAdapter
    private val cropViewModel: CropViewModel by viewModels {
        CropViewModelFactory((application as KrishakApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use Toolbar as standalone, no setSupportActionBar to avoid conflicts
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.action_investor -> {
                    startActivity(Intent(this, InvestorDashboardActivity::class.java))
                    true
                }
                R.id.action_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.action_farmer_listings -> {
                     cropViewModel.setCategoryFilter("MY_LISTINGS")
                     android.widget.Toast.makeText(this, "Showing My Listings", android.widget.Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        adapter = CropAdapter { crop ->
            val intent = Intent(this, CropDetailActivity::class.java)
            intent.putExtra("CROP_ID", crop.id)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        cropViewModel.allCrops.observe(this) { crops ->
            adapter.submitList(crops)
        }

        binding.fabAddCrop.setOnClickListener {
            startActivity(Intent(this, AddCropActivity::class.java))
        }

        setupCategoryListeners()
        setupSearchListener()
        setupUserLocation()
        
        binding.tvHeader.setOnClickListener {
            cropViewModel.setCategoryFilter(null)
        }
    }

    private fun setupUserLocation() {
        val userId = "farmer123" // Hardcoded for demo, match with ProfileActivity
        lifecycleScope.launch {
            val app = application as KrishakApplication
            // Verify app.repository and userDao logic
            val userFlow = app.repository.getUser(userId)
            
            userFlow?.collect { user ->
                if (user != null) {
                     // Requires android:id="@+id/tvLocation" in activity_main.xml
                     binding.tvLocation.text = user.address
                }
            }
        }
    }

    private fun setupCategoryListeners() {
        binding.categoryAll.setOnClickListener {
            cropViewModel.setCategoryFilter(null)
        }
        binding.categoryGrains.setOnClickListener {
            cropViewModel.setCategoryFilter("Grains")
        }
        binding.categorySeeds.setOnClickListener {
            cropViewModel.setCategoryFilter("Seeds")
        }
    }

    private fun setupSearchListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                cropViewModel.setSearchQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cropViewModel.setSearchQuery(newText ?: "")
                return true
            }
        })
    }
}