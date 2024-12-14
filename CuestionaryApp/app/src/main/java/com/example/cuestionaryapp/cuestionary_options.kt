package com.example.cuestionaryapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CuestionaryOptions : AppCompatActivity() {
    private var countQuiz = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cuestionary_options)

        val btn_qualification = findViewById<Button>(R.id.btn_qualifications)
        val btn_cancel = findViewById<Button>(R.id.btn_cancel)
        val lbl_user = findViewById<TextView>(R.id.lbl_user)
        val lbl_counter = findViewById<TextView>(R.id.lbl_counter)
        val rdgSignatures = findViewById<RadioGroup>(R.id.rdg_signatures)
        var itemSelectedId = -1

        val (userName, quizFinished) = getValuesFromOtherActivities()
        blockItemAndIncrementQuiz(quizFinished)
        setValuesInLabels(lbl_user, userName, lbl_counter)

        // Configurar evento para RadioGroup
        rdgSignatures.setOnCheckedChangeListener { _, selectedId ->
            itemSelectedId = selectedId
            if (selectedId == -1) {
                Toast.makeText(this, "Por favor selecciona una materia", Toast.LENGTH_SHORT).show()
            } else {
                handleRadioButtonSelection(selectedId, userName.toString())
            }
        }

        btn_cancel.setOnClickListener {
           navigateToActivity(MainActivity::class.java)
        }
        btn_qualification.setOnClickListener {
            navigateToActivity(Qualifications::class.java)
        }
    }

    private fun setValuesInLabels(lbl_user: TextView,userName: String?,lbl_counter: TextView) {
        lbl_user.text = "Hola, ${userName}"
        lbl_counter.text = "Evualaciones completadas: ${countQuiz}"
    }

    private fun blockItemAndIncrementQuiz(quizFinished: String) {
        val itemId = intent.getIntExtra("item_id", -1)

        if (quizFinished == "true" && itemId != -1) {
            incrementQuizFinish(itemId)
        }
    }

    private fun getValuesFromOtherActivities(): Pair<String?, String> {
        val userName = intent.getStringExtra("user_name")
        val quizFinished = intent.getStringExtra("quiz_finished") ?: "false"
        return Pair(userName, quizFinished)
    }

    private fun incrementQuizFinish(itemId: Int):Int {
        val selectedRadioButton: RadioButton = findViewById(itemId)
        selectedRadioButton.isEnabled = false
        return countQuiz++
    }

    private fun handleRadioButtonSelection(selectedId: Int, userName: String) {
        val subjectName = when (selectedId) {
            R.id.rdb_matemathics -> "Matemáticas"
            R.id.rdb_english -> "Inglés"
            R.id.rdb_informatic -> "Informática"
            R.id.rdb_history -> "Historia del Ecuador"
            R.id.rdb_app -> "Aplicaciones Móviles"
            else -> null
        }

        if (subjectName != null) {
            navigateToActivity(getActivityClass(selectedId), userName, subjectName,selectedId=selectedId)
        } else {
            Toast.makeText(this, "Opción no válida", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getActivityClass(selectedId: Int): Class<*> {
        return when (selectedId) {
            R.id.rdb_matemathics -> Matemathics::class.java
            R.id.rdb_english -> English::class.java
            R.id.rdb_informatic -> Computation::class.java
            R.id.rdb_history -> History::class.java
            R.id.rdb_app -> AppMovil::class.java
            else -> MainActivity::class.java
        }
    }

    private fun navigateToActivity(
        targetActivity: Class<*>,
        userName: String? = null,
        subjectName: String? = null,
        selectedId: Int? = 0
    ) {
        val intent = Intent(this, targetActivity)

        //let es para verificar si el parametro no es null. Si tiene un valor, se agrega al Intent como un extra
        userName?.let { intent.putExtra("user_name", userName) }
        subjectName?.let { intent.putExtra("subject_name", subjectName) }
        intent.putExtra("item_id", selectedId)
        startActivity(intent)
    }
}