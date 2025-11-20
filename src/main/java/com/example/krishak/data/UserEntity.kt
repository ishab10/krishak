package com.example.krishak.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val role: String, // "farmer", "buyer", "investor"
    val locationLat: Double,
    val locationLon: Double
)