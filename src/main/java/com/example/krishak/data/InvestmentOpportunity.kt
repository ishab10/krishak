package com.example.krishak.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvestmentOpportunity(
    val id: String,
    val title: String,
    val location: String,
    val duration: String,
    val roi: String,
    val risk: String, // "High", "Medium", "Low"
    val description: String = "",
    val minInvestment: Double = 0.0,
    val farmerName: String = ""
) : Parcelable