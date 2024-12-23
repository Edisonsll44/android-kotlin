package com.example.agenda24_25

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    private lateinit var btn_persona: Button
    private lateinit var btn_contactos: Button
    private lateinit var btn_salir: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        btn_persona = findViewById(R.id.btnPersonas)
        btn_contactos = findViewById(R.id.btnContactos)
        btn_salir = findViewById(R.id.btnSalir)




        btn_persona.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        btn_contactos.setOnClickListener {
            val intent = Intent(this,ContactoActivity::class.java)
            startActivity(intent)
        }

        btn_salir.setOnClickListener {
            finish()
        }
    }
}