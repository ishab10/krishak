package com.example.krishak

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.krishak.databinding.ActivityPaymentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val amount = intent.getDoubleExtra("AMOUNT", 0.0)
        binding.tvAmount.text = "₹$amount"

        binding.toolbar.setNavigationOnClickListener { finish() }
        
        setupPaymentMethods()

        binding.btnPay.setOnClickListener {
            processPayment()
        }
    }
    
    private fun setupPaymentMethods() {
        binding.rgPaymentMethods.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbUPI -> {
                    binding.layoutUpiDetails.visibility = View.VISIBLE
                    binding.layoutCardDetails.visibility = View.GONE
                }
                R.id.rbCard -> {
                    binding.layoutUpiDetails.visibility = View.GONE
                    binding.layoutCardDetails.visibility = View.VISIBLE
                }
                R.id.rbNetBanking -> {
                    binding.layoutUpiDetails.visibility = View.GONE
                    binding.layoutCardDetails.visibility = View.GONE
                }
            }
        }
    }
    
    private fun processPayment() {
        // Validate inputs
        if (binding.rbUPI.isChecked && binding.etUpiId.text.isNullOrEmpty()) {
            binding.etUpiId.error = "Enter UPI ID"
            return
        }
        
        if (binding.rbCard.isChecked) {
            if (binding.etCardNumber.text.isNullOrEmpty()) {
                binding.etCardNumber.error = "Enter Card Number"
                return
            }
            if (binding.etExpiry.text.isNullOrEmpty()) {
                binding.etExpiry.error = "Enter Expiry"
                return
            }
            if (binding.etCvv.text.isNullOrEmpty()) {
                binding.etCvv.error = "Enter CVV"
                return
            }
        }
        
        // Simulate backend processing
        binding.btnPay.isEnabled = false
        binding.btnPay.text = "Processing..."
        binding.progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            delay(2000) // Simulate network delay
            
            binding.progressBar.visibility = View.GONE
            binding.btnPay.isEnabled = true
            binding.btnPay.text = "Pay Securely"
            
            Toast.makeText(this@PaymentActivity, "Payment Successful!", Toast.LENGTH_LONG).show()
            setResult(RESULT_OK)
            finish()
        }
    }
}