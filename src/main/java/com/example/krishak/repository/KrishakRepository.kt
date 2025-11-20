package com.example.krishak.repository

import com.example.krishak.data.CartDao
import com.example.krishak.data.CartItemEntity
import com.example.krishak.data.CropDao
import com.example.krishak.data.CropEntity
import com.example.krishak.data.OrderDao
import com.example.krishak.data.OrderEntity
import com.example.krishak.data.UserDao
import com.example.krishak.data.UserEntity
import kotlinx.coroutines.flow.Flow

class KrishakRepository(
    private val cropDao: CropDao,
    private val userDao: UserDao? = null,
    private val orderDao: OrderDao? = null,
    private val cartDao: CartDao? = null
) {

    val allCrops: Flow<List<CropEntity>> = cropDao.getAllCrops()

    suspend fun insert(crop: CropEntity) {
        cropDao.insertCrop(crop)
    }

    suspend fun getCropById(id: String): CropEntity? {
        return cropDao.getCropById(id)
    }

    fun getFeaturedCrops(): Flow<List<CropEntity>> {
        return cropDao.getAllCrops()
    }

    fun getMandiPrices(): List<String> {
         return listOf("Wheat: ₹2100/q", "Rice: ₹1950/q", "Maize: ₹1800/q", "Soybean: ₹4200/q")
    }
    
    fun getCategories(): List<String> {
        return listOf("Grains", "Seeds", "Vegetables", "Fruits")
    }

    fun getSuggestedPrice(cropName: String, location: String): Double {
        return when {
            cropName.contains("Wheat", true) -> 22.0
            cropName.contains("Rice", true) -> 35.0
            cropName.contains("Soybean", true) -> 45.0
            cropName.contains("Tomato", true) -> 15.0
            else -> 20.0
        }
    }

    suspend fun updateCropPricing(id: String, price: Double, stock: Int, unit: String) {
        cropDao.updatePricing(id, price, stock, unit)
    }
    
    fun getUser(id: String): Flow<UserEntity?>? {
        return userDao?.getUser(id)
    }

    suspend fun updateProfile(id: String, name: String, phone: String, address: String) {
        userDao?.updateProfile(id, name, phone, address)
    }
    
    suspend fun insertUser(user: UserEntity) {
        userDao?.insertUser(user)
    }

    suspend fun insertOrder(order: OrderEntity) {
        orderDao?.insertOrder(order)
    }

    fun getOrders(userId: String): Flow<List<OrderEntity>>? {
        return orderDao?.getOrdersByUser(userId)
    }
    
    // Cart Methods
    suspend fun addToCart(item: CartItemEntity) {
        cartDao?.insertCartItem(item)
    }

    fun getCartItems(): Flow<List<CartItemEntity>>? {
        return cartDao?.getAllCartItems()
    }

    suspend fun clearCart() {
        cartDao?.clearCart()
    }
}