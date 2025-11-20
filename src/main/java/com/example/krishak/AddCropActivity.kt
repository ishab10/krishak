package com.example.krishak

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.krishak.data.CropEntity
import com.example.krishak.databinding.ActivityAddCropBinding
import com.example.krishak.viewmodel.CropViewModel
import com.example.krishak.viewmodel.CropViewModelFactory
import java.util.UUID

class AddCropActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCropBinding
    private val cropViewModel: CropViewModel by viewModels {
        CropViewModelFactory((application as KrishakApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()
        setupListeners()
    }

    private fun setupSpinners() {
        // Categories
        val categories = listOf("Grains", "Vegetables", "Fruits", "Seeds", "Other")
        val categoryAdapter = ArrayAdapter(this, R.layout.spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        // Units
        val units = listOf("kg", "quintal", "ton", "piece")
        val unitAdapter = ArrayAdapter(this, R.layout.spinner_item, units)
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerUnit.adapter = unitAdapter
    }

    private fun setupListeners() {
        // Auto-suggest Price
        binding.btnAutoPrice.setOnClickListener {
            val cropName = binding.etCropName.text.toString()
            if (cropName.isNotEmpty()) {
                val suggestedPrice = cropViewModel.getSuggestedPrice(cropName, "Nagpur")
                binding.etPrice.setText(suggestedPrice.toString())
                Toast.makeText(this, "Price suggested based on mandi rates", Toast.LENGTH_SHORT).show()
            } else {
                binding.etCropName.error = "Enter crop name first"
            }
        }

        // Save Crop
        binding.btnSaveCrop.setOnClickListener {
            val name = binding.etCropName.text.toString()
            val quantityString = binding.etQuantity.text.toString()
            val priceString = binding.etPrice.text.toString()
            val description = binding.etDescription.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val unit = binding.spinnerUnit.selectedItem.toString()

            if (name.isNotEmpty() && quantityString.isNotEmpty() && priceString.isNotEmpty()) {
                val price = priceString.toDoubleOrNull() ?: 0.0
                val quantity = quantityString.toIntOrNull() ?: 0
                
                val crop = CropEntity(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    category = category,
                    farmerId = "current_user_id", // Replace with actual auth ID
                    description = description,
                    price = price,
                    stock = quantity,
                    unit = unit,
                    sellerName = "Ram Kishan", // Placeholder for current user name
                    distance = "0 km",
                    rating = 0.0f,
                    imageRes = android.R.drawable.ic_menu_gallery, // Placeholder
                    variety = "" // Optional field
                )
                
                cropViewModel.insert(crop)
                Toast.makeText(this, "Crop listed for sale!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}