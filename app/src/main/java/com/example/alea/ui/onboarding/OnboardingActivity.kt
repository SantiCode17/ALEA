package com.example.alea.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.alea.R
import com.example.alea.databinding.ActivityOnboardingBinding
import com.example.alea.ui.auth.AuthActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    private data class OnboardingSlide(val imageRes: Int, val title: String, val description: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slides = listOf(
            OnboardingSlide(R.drawable.oso_alea_de_pie_saludando, getString(R.string.onboarding_title_1), getString(R.string.onboarding_desc_1)),
            OnboardingSlide(R.drawable.oso_alea_haciendo_ejercicio_sinfondo, getString(R.string.onboarding_title_2), getString(R.string.onboarding_desc_2)),
            OnboardingSlide(R.drawable.oso_alea_sentado_sofa_calendario_sinfondo, getString(R.string.onboarding_title_3), getString(R.string.onboarding_desc_3))
        )

        binding.onboardingViewpager.adapter = SlideAdapter(slides)
        setupDots(slides.size)
        binding.onboardingViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position, slides.size)
                binding.btnOnboardingNext.text = if (position == slides.size - 1)
                    getString(R.string.onboarding_start) else getString(R.string.onboarding_next)
                binding.btnOnboardingSkip.visibility = if (position == slides.size - 1) View.GONE else View.VISIBLE
            }
        })

        binding.btnOnboardingNext.setOnClickListener {
            val current = binding.onboardingViewpager.currentItem
            if (current < slides.size - 1) {
                binding.onboardingViewpager.currentItem = current + 1
            } else {
                finishOnboarding()
            }
        }

        binding.btnOnboardingSkip.setOnClickListener { finishOnboarding() }
    }

    private fun finishOnboarding() {
        getSharedPreferences("alea_prefs", MODE_PRIVATE).edit()
            .putBoolean("is_first_launch", false).apply()
        startActivity(Intent(this, AuthActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun setupDots(count: Int) {
        binding.onboardingDots.removeAllViews()
        repeat(count) { i ->
            val dot = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(if (i == 0) 24 else 8, 8).apply {
                    marginStart = 4; marginEnd = 4
                }
                background = ContextCompat.getDrawable(context,
                    if (i == 0) R.drawable.shape_chip_active else R.drawable.shape_chip_inactive)
            }
            binding.onboardingDots.addView(dot)
        }
    }

    private fun updateDots(selected: Int, count: Int) {
        for (i in 0 until count) {
            val dot = binding.onboardingDots.getChildAt(i)
            val params = dot.layoutParams as LinearLayout.LayoutParams
            params.width = if (i == selected) 24 else 8
            dot.layoutParams = params
            dot.background = ContextCompat.getDrawable(this,
                if (i == selected) R.drawable.shape_chip_active else R.drawable.shape_chip_inactive)
        }
    }

    private inner class SlideAdapter(private val slides: List<OnboardingSlide>) :
        RecyclerView.Adapter<SlideAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val image: ImageView = view.findViewById(R.id.onboarding_image)
            val title: TextView = view.findViewById(R.id.onboarding_title)
            val desc: TextView = view.findViewById(R.id.onboarding_description)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            VH(LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding_slide, parent, false))

        override fun onBindViewHolder(holder: VH, position: Int) {
            val slide = slides[position]
            holder.image.setImageResource(slide.imageRes)
            holder.title.text = slide.title
            holder.desc.text = slide.description
        }

        override fun getItemCount() = slides.size
    }
}
