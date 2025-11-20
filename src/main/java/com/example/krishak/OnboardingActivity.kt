package com.example.krishak

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.krishak.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnboardingItems()
        setupIndicators()
        setupCurrentIndicator(0)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setupCurrentIndicator(position)
                if (position == adapter.itemCount - 1) {
                    binding.btnNext.text = "Get Started"
                } else {
                    binding.btnNext.text = "Next"
                }
            }
        })

        binding.btnNext.setOnClickListener {
            if (binding.viewPager.currentItem + 1 < adapter.itemCount) {
                binding.viewPager.currentItem += 1
            } else {
                navigateToHome()
            }
        }

        binding.btnSkip.setOnClickListener {
            navigateToHome()
        }
    }

    private fun setupOnboardingItems() {
        val onboardingItems = listOf(
            OnboardingItem(
                android.R.drawable.ic_menu_myplaces, // Placeholder icon
                "Sell Directly",
                "List your crops and sell directly to buyers without middlemen."
            ),
            OnboardingItem(
                android.R.drawable.ic_menu_compass, // Placeholder icon
                "Real Prices",
                "Get transparent pricing and market trends for your produce."
            ),
            OnboardingItem(
                android.R.drawable.ic_secure, // Placeholder icon
                "Trusted Buyers",
                "Connect with verified buyers and retailers in your region."
            )
        )
        adapter = OnboardingAdapter(onboardingItems)
        binding.viewPager.adapter = adapter
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(adapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.indicator_inactive
                )
            )
            indicators[i]?.layoutParams = layoutParams
            binding.layoutIndicators.addView(indicators[i])
        }
    }

    private fun setupCurrentIndicator(index: Int) {
        val childCount = binding.layoutIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = binding.layoutIndicators.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}