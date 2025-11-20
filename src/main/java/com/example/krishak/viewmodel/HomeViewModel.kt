package com.example.krishak.viewmodel

import androidx.lifecycle.*
import com.example.krishak.data.CropEntity
import com.example.krishak.repository.KrishakRepository

class HomeViewModel(private val repository: KrishakRepository) : ViewModel() {

    val featuredCrops: LiveData<List<CropEntity>> = repository.getFeaturedCrops().asLiveData()

    private val _mandiPrices = MutableLiveData<List<String>>()
    val mandiPrices: LiveData<List<String>> = _mandiPrices

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        // In a real app, these would be suspend calls to fetching from API/DB
        _mandiPrices.value = repository.getMandiPrices()
        _categories.value = repository.getCategories()
    }

    fun searchCrops(query: String): LiveData<List<CropEntity>> {
        // In a real implementation, this would query the DB or API
        // For now, we just filter the existing list locally (simplified)
        // Note: This is a placeholder logic.
        return repository.allCrops.asLiveData().map { list ->
            list.filter { it.name.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) }
        }
    }
}

class HomeViewModelFactory(private val repository: KrishakRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}