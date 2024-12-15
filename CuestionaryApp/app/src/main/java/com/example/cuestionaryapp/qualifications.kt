package com.example.cuestionaryapp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuestionaryapp.dto.ScoresManager
import com.example.cuestionaryapp.dto.SubjectScore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Qualifications : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var average: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_qualifications)

        listView = findViewById(R.id.list_view)
        average = findViewById(R.id.text_Average)

        // Cargar la lista de puntajes
        loadAndDisplayScoresList()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadAndDisplayScoresList() {
        val scoresList = loadScoresList()

        // Crear un adaptador directamente en esta clase
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2,
            android.R.id.text1, scoresList.map { "${it.subjectName} : Calificación: ${it.score}/10" })
        listView.adapter = adapter

        // Calcular y mostrar el promedio
        val averageScore = calculateAverageScore(scoresList)
        average.text = "Promedio: $averageScore"
    }

    private fun loadScoresList(): ArrayList<SubjectScore> {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val json = sharedPreferences.getString("scores_list", null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<SubjectScore>>() {}.type
            Gson().fromJson(json, type)
        } else {
            arrayListOf() // Retorna una lista vacía si no hay datos
        }
    }

    override fun onPause() {
        super.onPause()
        // Limpia los datos almacenados en ScoresManager
        ScoresManager.scoresList.clear()

        // Elimina los datos de SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("scores_list") // Elimina la lista de puntajes
        editor.apply()
    }

    private fun calculateAverageScore(scoresList: List<SubjectScore>): Double {
        return if (scoresList.isNotEmpty()) {
            val totalScore = scoresList.sumOf { it.score }
            totalScore.toDouble() / scoresList.size
        } else {
            0.0
        }
    }
}