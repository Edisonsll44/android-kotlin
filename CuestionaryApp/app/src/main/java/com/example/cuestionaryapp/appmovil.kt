package com.example.cuestionaryapp

import Pregunta
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cuestionaryapp.dto.SubjectScore
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppMovil : AppCompatActivity() {
    private var currentQuestionIndex = 0
    private var correctQuestion = 0
    private var incorrectQuestion = 0
    private var userName = ""
    private var itemId = 0
    private lateinit var questions: List<Pregunta>
    private lateinit var customHeader: GenericHeader
    private lateinit var txtQuestion: TextView
    private lateinit var lblSuccess: TextView
    private lateinit var lblIncorrect: TextView
    private lateinit var llSumary: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_appmovil)

        // Recuperar el CustomHeader
        customHeader = findViewById(R.id.cmp_generic_header)
        userName = intent.getStringExtra("user_name") ?: "Usuario"
        val subjectName = intent.getStringExtra("subject_name") ?: "Materia desconocida"
        itemId = intent.getIntExtra("item_id", -1)

        // Verificar si el CustomHeader no es null antes de usarlo
        customHeader.apply {
            setGreeting(userName)
            setSubject(subjectName)
            incrementCounter()
        }

        //Obtengo la data
        questions = getData()

        // Obtener referencias a los elementos de la interfaz
        txtQuestion = findViewById(R.id.txt_question)
        val llOptions = findViewById<LinearLayout>(R.id.ll_options)
        val txtResponse = findViewById<TextView>(R.id.txt_response)
        val imgIcon = findViewById<ImageView>(R.id.iv_icon)
        lblSuccess = findViewById(R.id.lbl_approved)
        lblIncorrect = findViewById(R.id.lbl_incorrect)
        llSumary = findViewById(R.id.ll_sumary)

        llSumary.visibility = View.GONE

        // Mostrar la pregunta
        showQuestion(txtQuestion, llOptions, txtResponse, imgIcon)
    }

    private fun showQuestion(
        txtQuestion: TextView,
        llOptions: LinearLayout,
        txtResponse: TextView,
        imgIcon: ImageView
    ) {
        clearResponseAlert(txtResponse,imgIcon)

        val actualQuestion = questions[currentQuestionIndex]
        txtQuestion.text = "${currentQuestionIndex + 1}. ${actualQuestion.pregunta}"

        llOptions.removeAllViews()
        // Crear un RadioGroup con todas las opciones de la pregunta actual
        val rdbGroup = createRadioGroup(actualQuestion.opciones, txtResponse, imgIcon)
        llOptions.addView(rdbGroup)
    }

    private fun clearResponseAlert(txtResponse: TextView, imgIcon: ImageView){
        txtResponse.text = ""
        txtResponse.setBackgroundColor(Color.TRANSPARENT)
        imgIcon.visibility = ImageView.GONE
    }

    private fun createRadioGroup(options: List<String>, txtResponse: TextView, imgIcon: ImageView): RadioGroup {
        val radioGroup = RadioGroup(this)

        // Establecer padding
        val paddingInDp = 16
        val scale = resources.displayMetrics.density
        val paddingInPx = (paddingInDp * scale + 0.5f).toInt() // Convertir dp a px
        radioGroup.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

        for (option in options) {
            val radioButton = RadioButton(this)
            radioButton.text = option

            // Establecer padding para el RadioButton
            radioButton.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showResults(option, txtResponse, imgIcon)
                    goNextQuestion(txtResponse, imgIcon)
                }
            }
            radioGroup.addView(radioButton)
        }
        return radioGroup
    }

    private fun showResults(option: String, txtResponse: TextView, imgIcon: ImageView) {
        val actualQuestion = questions[currentQuestionIndex]
        if (option == actualQuestion.respuesta) {
            txtResponse.text = "Respuesta correcta."
            txtResponse.setTextColor(Color.WHITE)
            txtResponse.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            imgIcon.setImageResource(R.drawable.check)
            correctQuestion++
        } else {
            txtResponse.text = "Respuesta incorrecta."
            txtResponse.setTextColor(Color.WHITE)
            txtResponse.setBackgroundColor(Color.RED)
            imgIcon.setImageResource(R.drawable.close)
            incorrectQuestion++
        }

        // Configurar el icono
        imgIcon.visibility = ImageView.VISIBLE
        imgIcon.setPadding(8, 0, 8, 0)

        // Colocar el icono a la izquierda del texto
        txtResponse.setCompoundDrawablesWithIntrinsicBounds(imgIcon.drawable, null, null, null)
        txtResponse.compoundDrawablePadding = 8
    }

    private fun goNextQuestion(txtResponse: TextView, imgIcon: ImageView) {
        // Usar un Handler para esperar un momento antes de avanzar
        txtResponse.postDelayed({
            intent.getStringExtra("user_name") ?: "Usuario"
            currentQuestionIndex++
            if(currentQuestionIndex > 1)
            {
                customHeader.incrementCounter()
            }
            if (currentQuestionIndex < questions.size) {
                clearResponseAlert(txtResponse,imgIcon)
                showQuestion(findViewById(R.id.txt_question), findViewById(R.id.ll_options), txtResponse, imgIcon)
            } else {
                // Final del cuestionario
                txtQuestion.text = ""
                findViewById<LinearLayout>(R.id.ll_options).removeAllViews()
                clearResponseAlert(txtResponse,imgIcon)
                txtResponse.setBackgroundColor(Color.DKGRAY)
                txtResponse.text = "¡Has completado el cuestionario!"
                llSumary.visibility = View.VISIBLE
                lblSuccess.text = "Correctas: ${correctQuestion}"
                lblIncorrect.text = "Incorrectas: ${incorrectQuestion}"
                returnOptionQuiz()
            }
        }, 1500)
    }

    private fun returnOptionQuiz(){
        lifecycleScope.launch {
            delay(4000)
            val scoresList = arrayListOf(
                SubjectScore("Aplicaciones móviles",correctQuestion,itemId )
            )
            val intent = Intent(this@AppMovil, CuestionaryOptions::class.java)
            intent.putExtra("quiz_finished", "true")
            intent.putExtra("user_name", userName)
            intent.putParcelableArrayListExtra("scores_list", scoresList)
            startActivity(intent)
            finish()
        }
    }

    private fun getData(): List<Pregunta> {
        val inputStream = assets.open("data/quiz_app.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val preguntas = mutableListOf<Pregunta>()

        val gson = Gson()
        val jsonArray = gson.fromJson(jsonString, JsonArray::class.java)

        for (i in 0 until jsonArray.size()) {
            val jsonObject = jsonArray[i] as JsonObject
            val pregunta = jsonObject.get("pregunta").asString
            val opciones = jsonObject.getAsJsonArray("opciones").map { it.asString }
            val respuestaCorrecta = jsonObject.get("respuesta_correcta").asString
            preguntas.add(Pregunta(pregunta, opciones, respuestaCorrecta))
        }
        return preguntas
    }
}