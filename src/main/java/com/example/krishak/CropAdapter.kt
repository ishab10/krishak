package com.example.krishak

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.krishak.data.CropEntity
import com.example.krishak.databinding.ItemCropBinding

class CropAdapter(
    private val onCropClick: (CropEntity) -> Unit
) : ListAdapter<CropEntity, CropAdapter.CropViewHolder>(CropDiffCallback()) {

    inner class CropViewHolder(private val binding: ItemCropBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(crop: CropEntity) {
            val context = binding.root.context
            binding.tvCropName.text = crop.name
            binding.tvSellerName.text = context.getString(R.string.seller_info_format, crop.sellerName, crop.distance)
            binding.tvCropPrice.text = context.getString(R.string.price_format, crop.price.toString())
            binding.imgCropIcon.setImageResource(crop.imageRes)
            
            binding.root.setOnClickListener {
                onCropClick(crop)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropViewHolder {
        val binding = ItemCropBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CropViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CropDiffCallback : DiffUtil.ItemCallback<CropEntity>() {
        override fun areItemsTheSame(oldItem: CropEntity, newItem: CropEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CropEntity, newItem: CropEntity): Boolean {
            return oldItem == newItem
        }
    }
}