package com.example.agenda24_25

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class RequestHelper(private val context: Context) {

    // Función genérica para hacer solicitudes JSON (maneja JSONObject y JSONArray)
    fun makeJsonRequest(
        url: String,
        method: Int,
        jsonBody: JSONObject? = null, // Para solicitudes POST/PUT
        objectListener: ((JSONObject) -> Unit)? = null, // Para respuestas tipo JSONObject
        arrayListener: ((JSONArray) -> Unit)? = null, // Para respuestas tipo JSONArray
        errorListener: (VolleyError) -> Unit // Para manejar errores
    ) {
        when {
            objectListener != null -> {
                // Manejar respuestas JSONObject
                val jsonObjectRequest = JsonObjectRequest(
                    method,
                    url,
                    jsonBody,
                    { response -> objectListener(response) },
                    { error -> errorListener(error) }
                )
                Volley.newRequestQueue(context).add(jsonObjectRequest)
            }
            arrayListener != null -> {
                // Manejar respuestas JSONArray
                val jsonArrayRequest = JsonArrayRequest(
                    method,
                    url,
                    null,
                    { response -> arrayListener(response) },
                    { error -> errorListener(error) }
                )
                Volley.newRequestQueue(context).add(jsonArrayRequest)
            }
            else -> throw IllegalArgumentException("Se debe proporcionar al menos un listener (objectListener o arrayListener).")
        }
    }

    // Manejo de errores
    fun handleError(error: VolleyError) {
        if (error.networkResponse != null) {
            val statusCode = error.networkResponse.statusCode
            val errorMessage = String(error.networkResponse.data)
            try {
                if (errorMessage.trim().startsWith("[")) {
                    // Manejar JSONArray como error
                    Toast.makeText(context, "Error: Respuesta inesperada en formato JSONArray.", Toast.LENGTH_SHORT).show()
                } else {
                    val jsonError = JSONObject(errorMessage)
                    val message = jsonError.optString("message", "Error desconocido")
                    Toast.makeText(context, "Error $statusCode: $message", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error $statusCode: No se pudo procesar la respuesta.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Error en la conexión: ${error.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
