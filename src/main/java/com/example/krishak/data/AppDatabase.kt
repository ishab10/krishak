package com.example.krishak.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CropEntity::class, UserEntity::class, OrderEntity::class, CartItemEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cropDao(): CropDao
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "krishak_database"
                )
                .fallbackToDestructiveMigration() // Simplifies updates for dev
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}