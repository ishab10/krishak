package com.example.krishak

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.krishak.data.OrderEntity
import com.example.krishak.databinding.ActivityCartBinding
import kotlinx.coroutines.launch
import java.util.UUID

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter
    private var currentTotalAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeCartItems()

        binding.btnCheckout.setOnClickListener {
            if (currentTotalAmount <= 0) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val paymentMethod = if (binding.radioUpi.isChecked) "UPI" else "COD"
            
            lifecycleScope.launch {
                try {
                    val app = application as KrishakApplication
                    val orderId = UUID.randomUUID().toString()
                    val userId = "farmer123" 
                    
                    val order = OrderEntity(
                        id = orderId,
                        buyerId = userId,
                        cropId = "mixed_cart",
                        quantity = 1, // Represents one cart checkout
                        totalPrice = currentTotalAmount,
                        status = "Pending",
                        timestamp = System.currentTimeMillis()
                    )
                    
                    app.repository.insertOrder(order)
                    
                    // Clear cart after order
                    app.repository.clearCart()
                    
                    if (paymentMethod == "UPI") {
                        val intent = Intent(this@CartActivity, PaymentActivity::class.java)
                        intent.putExtra("AMOUNT", currentTotalAmount)
                        intent.putExtra("ORDER_ID", orderId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@CartActivity, "Order Placed Successfully! (COD)", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    
                } catch (e: Exception) {
                    Toast.makeText(this@CartActivity, "Failed to place order: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter { item ->
            // Optional: Handle item click or removal
        }
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCart.adapter = adapter
    }

    private fun observeCartItems() {
        lifecycleScope.launch {
            val app = application as KrishakApplication
            app.repository.getCartItems()?.collect { items ->
                adapter.submitList(items)
                
                // Calculate Total
                currentTotalAmount = items.sumOf { it.price * it.quantity }
                binding.tvCartTotal.text = "₹$currentTotalAmount"
            }
        }
    }
}