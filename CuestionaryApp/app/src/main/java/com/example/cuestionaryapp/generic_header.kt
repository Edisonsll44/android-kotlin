package com.example.cuestionaryapp

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

class GenericHeader  @JvmOverloads constructor (context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private var tvGreeting: TextView
    private var tvSubject: TextView
    private var tvCounter: TextView
    private var counterValue: Int = 0

    init {
        // coloca en la cabecera
        LayoutInflater.from(context).inflate(R.layout.activity_generic_header, this, true)
        orientation = VERTICAL

        // Referencias a los elementos del diseño
        tvGreeting = findViewById(R.id.tv_greeting)
        tvSubject = findViewById(R.id.tv_subject)
        tvCounter = findViewById(R.id.tv_counter)
    }

    // Establecer el saludo con el nombre del usuario
    fun setGreeting(name: String) {
        tvGreeting.text = "Hola, $name"
    }

    // Establecer la materia
    fun setSubject(subject: String) {
        tvSubject.text = "Materia: $subject"
    }

    // Incrementar el contador
    fun incrementCounter() {
        counterValue++
        tvCounter.text = "Preguntas resueltas: $counterValue"
    }

    // Reiniciar el contador
    fun resetCounter() {
        counterValue = 0
        tvCounter.text = "Preguntas resueltas: $counterValue"
    }
}
