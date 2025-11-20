package com.example.krishak

object CropRepository {
    private val crops = mutableListOf<Crop>()

    init {
        // Add some dummy data for "Featured Crops"
        crops.add(
            Crop(
                id = "1",
                name = "Organic Wheat",
                quantity = "500 kg",
                price = "2200",
                description = "High quality Sharbati wheat, grown without pesticides.",
                sellerName = "Ramesh Kumar",
                distance = "2 km",
                rating = 4.8f
            )
        )
        crops.add(
            Crop(
                id = "2",
                name = "Fresh Tomatoes",
                quantity = "100 kg",
                price = "1500",
                description = "Farm fresh red tomatoes, harvested today.",
                sellerName = "Suresh Patil",
                distance = "5 km",
                rating = 4.2f
            )
        )
         crops.add(
            Crop(
                id = "3",
                name = "Basmati Rice",
                quantity = "1000 kg",
                price = "4500",
                description = "Premium aged Basmati rice.",
                sellerName = "Kisan Agro",
                distance = "10 km",
                rating = 4.5f
            )
        )
    }

    fun addCrop(crop: Crop) {
        crops.add(crop)
    }

    fun getCrops(): List<Crop> {
        return crops.toList()
    }
    
    fun getCropById(id: String): Crop? {
        return crops.find { it.id == id }
    }
}