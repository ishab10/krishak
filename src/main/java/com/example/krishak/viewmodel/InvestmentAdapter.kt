package com.example.krishak.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.krishak.R
import com.example.krishak.data.InvestmentOpportunity
import com.example.krishak.databinding.ItemInvestmentOpportunityBinding

class InvestmentAdapter(
    private val opportunities: List<InvestmentOpportunity>,
    private val onInvestClick: (InvestmentOpportunity) -> Unit,
    private val onItemClick: (InvestmentOpportunity) -> Unit
) : RecyclerView.Adapter<InvestmentAdapter.InvestmentViewHolder>() {

    inner class InvestmentViewHolder(private val binding: ItemInvestmentOpportunityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(opportunity: InvestmentOpportunity) {
            binding.tvTitle.text = opportunity.title
            binding.tvLocationDuration.text = "${opportunity.location} • ${opportunity.duration}"
            binding.tvROI.text = opportunity.roi
            binding.tvRisk.text = opportunity.risk

            when (opportunity.risk) {
                "High" -> binding.tvRisk.setBackgroundResource(R.drawable.bg_risk_high)
                "Medium" -> binding.tvRisk.setBackgroundResource(R.drawable.bg_risk_medium)
                else -> binding.tvRisk.setBackgroundResource(R.drawable.bg_risk_low) // Assumes a bg_risk_low exists
            }
            
            binding.btnInvest.setOnClickListener { onInvestClick(opportunity) }
            binding.root.setOnClickListener { onItemClick(opportunity) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvestmentViewHolder {
        return InvestmentViewHolder(
            ItemInvestmentOpportunityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InvestmentViewHolder, position: Int) {
        holder.bind(opportunities[position])
    }

    override fun getItemCount(): Int = opportunities.size
}