package com.example.cuestionaryapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Computation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configurar el diseño
        setContentView(R.layout.activity_computation)

        // Recuperar el CustomHeader
        val customHeader = findViewById<GenericHeader>(R.id.cmp_generic_header)
        val userName = intent.getStringExtra("user_name") ?: "Usuario"
        val subjectName = intent.getStringExtra("subject_name") ?: "Materia desconocida"

        // Verificar si el CustomHeader no es null antes de usarlo
        customHeader?.apply {
            setGreeting(userName)
            setSubject(subjectName)
            incrementCounter()
        } ?: Log.e("Computación", "Error: CustomHeader es null")
    }
}