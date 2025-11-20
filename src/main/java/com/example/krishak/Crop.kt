package com.example.krishak

data class Crop(
    val id: String,
    val name: String,
    val quantity: String,
    val price: String,
    val description: String,
    val sellerName: String = "Ram Kishan",
    val distance: String = "5 km",
    val rating: Float = 4.5f,
    val imageRes: Int = android.R.drawable.ic_menu_gallery
)