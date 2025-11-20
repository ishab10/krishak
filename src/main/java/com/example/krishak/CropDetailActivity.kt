package com.example.krishak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.krishak.data.CartItemEntity
import com.example.krishak.databinding.ActivityCropDetailBinding
import com.example.krishak.viewmodel.CropViewModel
import com.example.krishak.viewmodel.CropViewModelFactory
import kotlinx.coroutines.launch

class CropDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCropDetailBinding
    
    private val cropViewModel: CropViewModel by viewModels {
        val app = application as KrishakApplication
        CropViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityCropDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.toolbar.title = ""
            binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
            binding.toolbar.setNavigationOnClickListener { finish() }

            val cropId = intent.getStringExtra("CROP_ID")
            
            if (cropId.isNullOrEmpty()) {
                Toast.makeText(this, "Error: Invalid Crop ID", Toast.LENGTH_SHORT).show()
                return
            }

            lifecycleScope.launch {
                try {
                    val crop = cropViewModel.getCrop(cropId)

                    if (crop != null) {
                        with(binding) {
                            tvDetailName.text = crop.name
                            tvDetailPrice.text = "₹${crop.price}"
                            chipStock.text = "In Stock: ${crop.stock} ${crop.unit}"
                            tvSellerName.text = crop.sellerName
                            tvDistance.text = "${crop.distance} away"
                            tvRating.text = crop.rating.toString()
                            tvDetailDescription.text = crop.description
                            try {
                                imgCropDetail.setImageResource(crop.imageRes)
                            } catch (e: Exception) {
                                Log.e("CropDetailActivity", "Image load error", e)
                            }
                            
                            btnAddCart.setOnClickListener {
                                lifecycleScope.launch {
                                    try {
                                        val app = application as KrishakApplication
                                        val cartItem = CartItemEntity(
                                            cropId = crop.id,
                                            cropName = crop.name,
                                            price = crop.price,
                                            quantity = 100, // Default/Demo quantity
                                            imageRes = crop.imageRes
                                        )
                                        app.repository.addToCart(cartItem)
                                        Toast.makeText(this@CropDetailActivity, "Added to Cart", Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(this@CropDetailActivity, "Failed to add: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            
                            btnBuyNow.setOnClickListener {
                                // Add to cart then open cart
                                lifecycleScope.launch {
                                    try {
                                        val app = application as KrishakApplication
                                        val cartItem = CartItemEntity(
                                            cropId = crop.id,
                                            cropName = crop.name,
                                            price = crop.price,
                                            quantity = 100,
                                            imageRes = crop.imageRes
                                        )
                                        app.repository.addToCart(cartItem)
                                        startActivity(Intent(this@CropDetailActivity, CartActivity::class.java))
                                    } catch (e: Exception) {
                                         Toast.makeText(this@CropDetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    } else {
                         Toast.makeText(this@CropDetailActivity, "Crop details not found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("CropDetailActivity", "Error fetching details", e)
                    Toast.makeText(this@CropDetailActivity, "Error loading details", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("CropDetailActivity", "Crash in onCreate", e)
            Toast.makeText(this, "Error opening details: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}