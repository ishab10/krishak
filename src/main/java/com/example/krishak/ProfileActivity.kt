package com.example.krishak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.krishak.databinding.ActivityProfileBinding
import com.example.krishak.databinding.DialogEditProfileBinding
import com.example.krishak.viewmodel.CropViewModel
import com.example.krishak.viewmodel.CropViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.Locale

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    
    // We reuse CropViewModel for now as it holds the repository instance
    // Ideally create a ProfileViewModel or UserViewModel
    private val viewModel: CropViewModel by viewModels {
        val app = application as KrishakApplication
        CropViewModelFactory(app.repository)
    }
    
    // Hardcoded User ID for demo
    private val userId = "farmer123" 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityProfileBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.toolbar.title = "My Profile"
            binding.toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
            binding.toolbar.setNavigationOnClickListener { finish() }
            
            // Observe user data
            loadUserProfile()
            
            binding.btnEditProfile.setOnClickListener {
                showEditProfileDialog()
            }

            binding.btnOrderHistory.setOnClickListener {
                startActivity(Intent(this, OrderHistoryActivity::class.java))
            }

        } catch (e: Exception) {
            Log.e("ProfileActivity", "Crash in onCreate", e)
            Toast.makeText(this, "Error opening profile: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun loadUserProfile() {
        lifecycleScope.launch {
            val app = application as KrishakApplication
            val userFlow = app.repository.getUser(userId)
            
            userFlow?.collect { user ->
                if (user != null) {
                    binding.tvProfileName.text = user.name
                    binding.tvProfilePhone.text = user.phone
                    binding.tvProfileLocation.text = user.address
                    binding.tvProfileRole.text = user.role.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                } else {
                    // Create default user if not exists
                    // Matches UserEntity(id, name, phone, address, role, lat, lon)
                    val defaultUser = com.example.krishak.data.UserEntity(
                        userId, "Ram Kishan", "+91 98765 43210", "Nagpur, Maharashtra", "farmer", 0.0, 0.0
                    )
                    app.repository.insertUser(defaultUser)
                }
            }
        }
    }

    private fun showEditProfileDialog() {
        try {
            val dialogBinding = DialogEditProfileBinding.inflate(LayoutInflater.from(this))
            
            dialogBinding.etEditName.setText(binding.tvProfileName.text)
            dialogBinding.etEditPhone.setText(binding.tvProfilePhone.text)
            dialogBinding.etEditLocation.setText(binding.tvProfileLocation.text)

            val dialog = MaterialAlertDialogBuilder(this)
                .setView(dialogBinding.root)
                .setCancelable(false)
                .create()

            dialogBinding.btnCancelEdit.setOnClickListener {
                dialog.dismiss()
            }

            dialogBinding.btnSaveEdit.setOnClickListener {
                val newName = dialogBinding.etEditName.text.toString()
                val newPhone = dialogBinding.etEditPhone.text.toString()
                val newLocation = dialogBinding.etEditLocation.text.toString()

                if (newName.isNotBlank() && newPhone.isNotBlank()) {
                    
                    // Update Backend
                    lifecycleScope.launch {
                        try {
                            val app = application as KrishakApplication
                            // Matches updateProfile(id, name, phone, address)
                            app.repository.updateProfile(userId, newName, newPhone, newLocation)
                            
                            Toast.makeText(this@ProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } catch (e: Exception) {
                             Toast.makeText(this@ProfileActivity, "Error saving: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Please fill name and phone", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.show()

        } catch (e: Exception) {
            Log.e("ProfileActivity", "Error showing dialog", e)
            Toast.makeText(this, "Error opening editor", Toast.LENGTH_SHORT).show()
        }
    }
}