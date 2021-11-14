package fr.mastersid.pic2.escapegame

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import fr.mastersid.pic2.escapegame.databinding.PopupWindowBinding

class PopUpWindow : AppCompatActivity() {
    private var popupText = ""

    private lateinit var _binding: PopupWindowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        this.title = "Help"
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)

        _binding = PopupWindowBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        // Get the data
        val bundle = intent.extras
        popupText = bundle?.getString("popuptext", "notice") ?: ""
        // Set the data
        _binding.popupWindowText.text = popupText

        // Fade animation for the background of Popup Window
        val alpha = 100 //between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.BLACK, alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), Color.TRANSPARENT, alphaColor)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            _binding.popupWindowBackground.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        // Fade animation for the Popup Window
        _binding.popupWindowView.alpha = 0f
        _binding.popupWindowView.animate().alpha(1f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // Close the Popup Window when you press the button
        _binding.popupWindowButton.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        // Fade animation for the background of Popup Window when you press the back button
        val alpha = 100 // between 0-255
        val alphaColor = ColorUtils.setAlphaComponent(Color.BLACK, alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, Color.TRANSPARENT)
        colorAnimation.duration = 500 // milliseconds
        colorAnimation.addUpdateListener { animator ->
            _binding.popupWindowBackground.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        // Fade animation for the Popup Window when you press the back button
        _binding.popupWindowView.animate().alpha(0f).setDuration(500).setInterpolator(
            DecelerateInterpolator()
        ).start()

        // After animation finish, close the Activity
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                finish()
                overridePendingTransition(0, 0)
            }
        })
        colorAnimation.start()
    }

}