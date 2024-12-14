package com.example.cuestionaryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btn_enter = findViewById<Button>(R.id.btn_intent)
        val txt_name = findViewById<EditText>(R.id.txt_name)

      btn_enter.setOnClickListener {
            val name = txt_name.text.toString().trim()

          if(name.isEmpty())
          {
              Toast.makeText(this,"Debe ingresar su nombre, para continuar", Toast.LENGTH_LONG).show()
          }else{
              val intent = Intent(this, CuestionaryOptions::class.java)
              intent.putExtra("user_name", name.uppercase())
              startActivity(intent)
          }
      }
    }
}