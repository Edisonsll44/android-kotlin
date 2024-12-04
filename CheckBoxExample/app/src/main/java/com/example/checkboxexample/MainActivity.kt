package com.example.checkboxexample

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
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

        var chkPapas = findViewById<CheckBox>(R.id.chk_papas)
        var chkQueso = findViewById<CheckBox>(R.id.chk_queso)
        var btnVerificar = findViewById<Button>(R.id.btn_Verificar)

        btnVerificar.setOnClickListener {
            var seleccion = ""

            if(chkPapas.isChecked)
            {
                seleccion += "papas extra "
            }

            if(chkQueso.isChecked)
            {
                 seleccion += "queso extra"
            }

            if(seleccion.equals(""))
            {
                Toast.makeText(applicationContext,"no ha selccionado ningún extra",Toast.LENGTH_SHORT).show()
            }else
            {
                Toast.makeText(applicationContext,"Usted a seleccionado: " + seleccion, Toast.LENGTH_SHORT).show()
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}