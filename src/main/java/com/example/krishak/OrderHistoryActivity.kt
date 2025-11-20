package com.example.krishak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.krishak.data.OrderEntity
import com.example.krishak.databinding.ActivityOrderHistoryBinding
import com.example.krishak.databinding.ItemOrderBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderHistoryBinding
    private val userId = "farmer123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "My Orders"
        binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.recyclerViewOrders.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val app = application as KrishakApplication
            app.repository.getOrders(userId)?.collect { orders ->
                if (orders.isNotEmpty()) {
                    binding.recyclerViewOrders.adapter = OrderAdapter(orders)
                }
            }
        }
    }

    class OrderAdapter(private val orders: List<OrderEntity>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

        class OrderViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return OrderViewHolder(binding)
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            val order = orders[position]
            holder.binding.tvOrderId.text = "Order ID: ${order.id.take(8)}"
            holder.binding.tvOrderStatus.text = order.status
            holder.binding.tvOrderDate.text = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(order.timestamp))
            holder.binding.tvOrderTotal.text = "₹${order.totalPrice}"
            // holder.binding.tvOrderItems.text = "${order.quantity} items" // Simplification
        }

        override fun getItemCount() = orders.size
    }
}