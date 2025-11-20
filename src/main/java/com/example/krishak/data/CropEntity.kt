package com.example.krishak.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crops")
data class CropEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // "Grains", "Seeds", etc.
    val farmerId: String,
    val description: String,
    
    // Pricing & Stock
    val price: Double,
    val stock: Int,
    val unit: String, // "kg", "quintal"
    
    // Display Info
    val sellerName: String,
    val distance: String,
    val rating: Float,
    val imageRes: Int, // For demo, we use resource IDs. In real app, use String (URL)
    val variety: String = ""
)