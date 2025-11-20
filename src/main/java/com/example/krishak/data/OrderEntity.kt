package com.example.krishak.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: String,
    val buyerId: String,
    val cropId: String,
    val quantity: Int,
    val totalPrice: Double,
    val status: String, // "pending", "completed"
    val timestamp: Long
)