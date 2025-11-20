package com.example.krishak.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CropDao {
    // Supports filtering via query logic in ViewModel, but we can add specific queries
    @Query("SELECT * FROM crops")
    fun getAllCrops(): Flow<List<CropEntity>>

    @Query("SELECT * FROM crops WHERE category = :category")
    fun getCropsByCategory(category: String): Flow<List<CropEntity>>

    @Query("SELECT * FROM crops WHERE id = :id")
    suspend fun getCropById(id: String): CropEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrop(crop: CropEntity)

    @Query("DELETE FROM crops")
    suspend fun clearAll()
    
    // Price Update Endpoint
    @Query("UPDATE crops SET price = :price, stock = :stock, unit = :unit WHERE id = :id")
    suspend fun updatePricing(id: String, price: Double, stock: Int, unit: String)
}