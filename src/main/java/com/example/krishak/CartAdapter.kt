package com.example.krishak

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.krishak.data.CartItemEntity
import com.example.krishak.databinding.ItemCartBinding

class CartAdapter(
    private val onDeleteClick: (CartItemEntity) -> Unit
) : ListAdapter<CartItemEntity, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItemEntity) {
            binding.tvCartItemName.text = item.cropName
            binding.tvCartItemPrice.text = "₹${item.price * item.quantity}"
            binding.tvCartItemQuantity.text = "${item.quantity} kg x ₹${item.price}"
            binding.imgCartItem.setImageResource(item.imageRes)
            
            // binding.btnRemove.setOnClickListener { onDeleteClick(item) } 
            // Assuming we might add a remove button later, for now just listing
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItemEntity>() {
        override fun areItemsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItemEntity, newItem: CartItemEntity): Boolean {
            return oldItem == newItem
        }
    }
}