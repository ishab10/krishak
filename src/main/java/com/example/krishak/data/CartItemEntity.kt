package com.example.krishak.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cropId: String,
    val cropName: String,
    val price: Double,
    val quantity: Int,
    val imageRes: Int
)