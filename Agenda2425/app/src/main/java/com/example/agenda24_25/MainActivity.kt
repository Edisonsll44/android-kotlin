package com.example.agenda24_25

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val codigo = ArrayList<String>()
    lateinit var txtcedula: EditText
    lateinit var txtnombre: EditText
    lateinit var txtapellido: EditText
    lateinit var txtclave: EditText
    lateinit var txtemail: EditText
    lateinit var lista: ListView
    lateinit var btnguardar : Button
    lateinit var btnconsultar : Button
    lateinit var btnactualizar : Button
    lateinit var btneliminar : Button
    lateinit var btncancelar : Button
    lateinit var btnnuevo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        mapeo()
        limpiar()
        bloquearCajas()
        inabilitarBotones()
        btnnuevo.isEnabled = true

        btnguardar.setOnClickListener {
            if (areFieldsEmpty(txtcedula, txtnombre, txtemail, txtapellido, txtclave))
            {
                Toast.makeText(applicationContext, "Faltan datos", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext, "Datos guardados exitosamente", Toast.LENGTH_LONG).show()
                limpiar()
                bloquearCajas()
                inabilitarBotones()
                btnnuevo.isEnabled = true
            }
        }

        btncancelar.setOnClickListener {
            limpiar()
            bloquearCajas()
            inabilitarBotones()
            btnnuevo.isEnabled = true
        }

        btnnuevo.setOnClickListener {
            inabilitarBotones()
            btnguardar.isEnabled = true
            btncancelar.isEnabled = true
            activarCajas()
        }
    }

    fun activarCajas(){
        txtcedula.isEnabled = true
        txtnombre.isEnabled = true
        txtapellido.isEnabled = true
        txtclave.isEnabled = true
        txtemail.isEnabled = true
    }

    fun bloquearCajas(){
        txtcedula.isEnabled = false
        txtnombre.isEnabled = false
        txtapellido.isEnabled = false
        txtclave.isEnabled = false
        txtemail.isEnabled = false

    }

    fun mapeo(){
        txtcedula = findViewById(R.id.txt_cedula)
        txtnombre = findViewById(R.id.txt_nombre)
        txtapellido = findViewById(R.id.txt_apellido)
        txtclave = findViewById(R.id.txt_clave)
        txtemail = findViewById(R.id.txt_email)
        lista = findViewById(R.id.lst_personas)


        btnnuevo = findViewById(R.id.btn_nuevo)
        btncancelar = findViewById(R.id.btn_cancelar)
        btnactualizar = findViewById(R.id.btn_actualizar)
        btnguardar = findViewById(R.id.btn_guardar)
        btnconsultar = findViewById(R.id.btn_consultar)
        btneliminar = findViewById(R.id.btn_eliminar)
    }

    fun limpiar(){
        txtcedula.setText("")
        txtnombre.setText("")
        txtapellido.setText("")
        txtclave.setText("")
        txtemail.setText("")
    }

    fun inabilitarBotones(){
        btnguardar.isEnabled = false
        btnconsultar.isEnabled = false
        btneliminar.isEnabled = false
        btnactualizar.isEnabled = false
        btnnuevo.isEnabled = false
        btncancelar.isEnabled = false

    }

    private fun areFieldsEmpty(vararg fields: EditText): Boolean {
        return fields.any { it.text.toString().isBlank() }
    }
}