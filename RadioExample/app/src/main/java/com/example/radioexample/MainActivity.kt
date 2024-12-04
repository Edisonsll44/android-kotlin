package com.example.radioexample

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var sexo = "no seleccionado";
        val rg:RadioGroup = findViewById(R.id.rg) as RadioGroup;
        rg.setOnCheckedChangeListener { group, checkedId ->
                sexo = when(checkedId) {
                    R.id.rb_masculino -> "Masculino"
                    R.id.rb_femenino -> "Femenino"
                    R.id.rb_otros -> "Otros"
                else -> "no seleccionado"
                }
        }

        val btnVerificar = findViewById<Button>(R.id.btn_ejecutar);
            btnVerificar.setOnClickListener {
                Toast.makeText(applicationContext, sexo, Toast.LENGTH_SHORT).show()
            }





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}


