package com.example.krishak

import android.app.Application
import com.example.krishak.data.AppDatabase
import com.example.krishak.repository.KrishakRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class KrishakApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this) }
    // Pass all DAOs to repository
    val repository by lazy { 
        KrishakRepository(
            database.cropDao(), 
            database.userDao(),
            database.orderDao(),
            database.cartDao()
        ) 
    }
}