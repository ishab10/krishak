package com.example.krishak.viewmodel

import androidx.lifecycle.*
import com.example.krishak.data.CropEntity
import com.example.krishak.repository.KrishakRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow

class CropViewModel(private val repository: KrishakRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _categoryFilter = MutableStateFlow<String?>(null)

    val allCrops: LiveData<List<CropEntity>> = combine(
        repository.allCrops,
        _searchQuery,
        _categoryFilter
    ) { crops, query, category ->
        crops.filter { crop ->
            val matchesQuery = crop.name.contains(query, ignoreCase = true) || 
                             crop.description.contains(query, ignoreCase = true)
            
            // Check for "MY_LISTINGS" filter
            val matchesCategory = if (category == "MY_LISTINGS") {
                // TODO: Replace with actual user ID check when authentication is implemented
                // For demo, we assume the current user is "farmer123" (from AddCropActivity usually)
                crop.farmerId == "farmer123"
            } else {
                category == null || 
                crop.category.equals(category, ignoreCase = true) ||
                crop.description.contains(category, ignoreCase = true) || 
                crop.name.contains(category, ignoreCase = true)
            }
            
            matchesQuery && matchesCategory
        }
    }.asLiveData()

    fun insert(crop: CropEntity) = viewModelScope.launch {
        repository.insert(crop)
    }
    
    suspend fun getCrop(id: String): CropEntity? {
        return repository.getCropById(id)
    }

    fun setCategoryFilter(category: String?) {
        _categoryFilter.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun getSuggestedPrice(cropName: String, location: String): Double {
        return repository.getSuggestedPrice(cropName, location)
    }
}

class CropViewModelFactory(private val repository: KrishakRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CropViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CropViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}