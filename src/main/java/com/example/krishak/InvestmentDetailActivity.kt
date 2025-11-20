package com.example.krishak

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.krishak.data.InvestmentOpportunity
import com.example.krishak.databinding.ActivityInvestmentDetailBinding

class InvestmentDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInvestmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvestmentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val opportunity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("INVESTMENT_OPPORTUNITY", InvestmentOpportunity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("INVESTMENT_OPPORTUNITY")
        }

        if (opportunity != null) {
            setupUI(opportunity)
        } else {
            Toast.makeText(this, "Error loading opportunity details", Toast.LENGTH_SHORT).show()
            finish()
        }
        
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupUI(opportunity: InvestmentOpportunity) {
        with(binding) {
            tvTitle.text = opportunity.title
            tvFarmerName.text = "By ${opportunity.farmerName}"
            tvRisk.text = "${opportunity.risk} Risk"
            tvLocation.text = opportunity.location
            tvDescription.text = opportunity.description
            tvROI.text = opportunity.roi
            tvDuration.text = opportunity.duration
            btnInvestNow.text = "Invest Now (Min ₹${opportunity.minInvestment})"
            
            when (opportunity.risk) {
                "High" -> tvRisk.setBackgroundResource(R.drawable.bg_risk_high)
                "Medium" -> tvRisk.setBackgroundResource(R.drawable.bg_risk_medium)
                else -> tvRisk.setBackgroundResource(R.drawable.bg_risk_low)
            }

            btnInvestNow.setOnClickListener {
                 val intent = Intent(this@InvestmentDetailActivity, PaymentActivity::class.java)
                 intent.putExtra("AMOUNT", opportunity.minInvestment)
                 startActivity(intent)
            }
        }
    }
}