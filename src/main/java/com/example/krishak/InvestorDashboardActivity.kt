package com.example.krishak

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krishak.data.InvestmentOpportunity
import com.example.krishak.databinding.ActivityInvestorDashboardBinding
import com.example.krishak.viewmodel.InvestmentAdapter

class InvestorDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInvestorDashboardBinding
    private lateinit var adapter: InvestmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvestorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        
        binding.btnFilter.setOnClickListener {
            Toast.makeText(this, "Filter functionality coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        // Dummy data for demonstration
        val opportunities = listOf(
            InvestmentOpportunity(
                id = "1",
                title = "Organic Soy Farming",
                location = "Vidarbha Region",
                duration = "6 Months Duration",
                roi = "18-22%",
                risk = "High",
                description = "Support local farmers in transitioning to organic soy farming. High yield expected due to favorable monsoon predictions.",
                minInvestment = 5000.0,
                farmerName = "Kisan Coop Society"
            ),
            InvestmentOpportunity(
                id = "2",
                title = "Hydroponic Setup",
                location = "Pune Outskirts",
                duration = "3 Years Duration",
                roi = "15%",
                risk = "Medium",
                description = "Investment in automated hydroponic units for exotic vegetables supply to city restaurants.",
                minInvestment = 25000.0,
                farmerName = "GreenTech Farms"
            ),
             InvestmentOpportunity(
                id = "3",
                title = "Wheat Cultivation",
                location = "Punjab",
                duration = "4 Months Duration",
                roi = "12-15%",
                risk = "Low",
                description = "Traditional wheat cultivation with insured crops. Steady returns and low risk profile.",
                minInvestment = 10000.0,
                farmerName = "Singh Brothers Agro"
            )
        )

        adapter = InvestmentAdapter(opportunities, { opportunity ->
            Toast.makeText(this, "Investing in ${opportunity.title}...", Toast.LENGTH_SHORT).show()
        }, { opportunity ->
             val intent = Intent(this, InvestmentDetailActivity::class.java)
             intent.putExtra("INVESTMENT_OPPORTUNITY", opportunity)
             startActivity(intent)
        })
        
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}