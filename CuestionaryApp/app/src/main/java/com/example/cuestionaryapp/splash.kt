package com.example.cuestionaryapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Animar el texto
        val animatedText = findViewById<TextView>(R.id.tv_animated_text)
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_out)
        animatedText.startAnimation(animation)

        // Splash Delay para cambiar de actividad
        animatedText.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 5000)
    }
}