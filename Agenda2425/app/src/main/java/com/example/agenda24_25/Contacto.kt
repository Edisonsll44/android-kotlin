package com.example.agenda24_25

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.example.agenda24_25.dto.ContactoDto
import com.example.agenda24_25.dto.PersonaDto
import org.json.JSONArray
import org.json.JSONObject
import requestHelper.Constants

class ContactoActivity : AppCompatActivity() {

    private lateinit var spnn_personas: Spinner
    private lateinit var requestHelper: RequestHelper
    private lateinit var lstPersonas: ListView
    private lateinit var txtNombre: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtTelefono: EditText

    private lateinit var btnGrabar: Button
    private lateinit var btnConsultar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnNuevo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contacto)

        spnn_personas = findViewById(R.id.spinner_persona)
        lstPersonas = findViewById(R.id.lst_personas)

        txtNombre = findViewById(R.id.txt_nombre)
        txtApellido = findViewById(R.id.txt_apellido)
        txtEmail = findViewById(R.id.txt_email)
        txtTelefono = findViewById(R.id.txt_telefono)

        btnConsultar = findViewById(R.id.btn_consultar)
        btnCancelar = findViewById(R.id.btn_cancelar)
        btnGrabar = findViewById(R.id.btn_guardar)
        btnActualizar = findViewById(R.id.btn_actualizar)
        btnEliminar = findViewById(R.id.btn_eliminar)
        btnNuevo = findViewById(R.id.btn_nuevo)

        inabilitarBotones()

        btnNuevo.isEnabled = true
        btnConsultar.isEnabled = true
        btnCancelar.isEnabled = true

        requestHelper = RequestHelper(this)

        // Cargar las personas
        loadPersons()



        btnConsultar.setOnClickListener {
            loadContacts()
        }

        btnGrabar.setOnClickListener {
            createContact()
        }

        btnCancelar.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        btnActualizar.setOnClickListener {
            updateContact()
        }

        btnEliminar.setOnClickListener {
            deleteContact(8)
        }

        btnNuevo.setOnClickListener {
            btnGrabar.isEnabled = true
        }

        lstPersonas.setOnItemClickListener { parent, view, position, id ->
            val selectedContact = parent.getItemAtPosition(position) as String

            // Obtener los datos de la fila seleccionada
            val selectedContactDto = getContactDtoFromSelectedContact(selectedContact)

            // Poblar los campos de texto con los datos del contacto
            txtNombre.setText(selectedContactDto.nombreContacto)
            txtApellido.setText(selectedContactDto.apellidoContacto)
            txtEmail.setText(selectedContactDto.emailContacto)
            txtTelefono.setText(selectedContactDto.telefonoContacto)


            // Seleccionar el valor en el Spinner basado en la persona asociada al contacto
            selectPersonInSpinner(selectedContactDto.idPersona)

            btnActualizar.isEnabled = true
            btnEliminar.isEnabled = true

        }
    }

    private fun inabilitarBotones(){
        btnGrabar.isEnabled = false
        btnConsultar.isEnabled = false
        btnEliminar.isEnabled = false
        btnActualizar.isEnabled = false
        btnNuevo.isEnabled = false
        btnCancelar.isEnabled = false
    }

    private fun createContact() {
        val url = Constants.CONTACTOS_URL
        if(!validatePreviewSave()) {
            // Crear el objeto JSON para el cuerpo de la solicitud
            val jsonBody = JSONObject()
            jsonBody.put("apellido_contacto", txtApellido.text.toString())
            jsonBody.put("email_contacto", txtEmail.text.toString())
            jsonBody.put("nombre_contacto", txtNombre.text.toString())
            jsonBody.put("telefono_contacto", txtTelefono.text.toString())
            jsonBody.put("id_persona", getSelectedPersonId())
            jsonBody.put("cedula_persona", "")
            jsonBody.put("id", 0)

            // Usar RequestHelper para hacer la solicitud
            requestHelper.makeJsonRequest(
                url,
                Request.Method.POST,
                jsonBody,
                objectListener = { response -> handleResponseContactResponse(response) },
                errorListener = { error -> requestHelper.handleError(error) }
            )
        }
    }

    private fun updateContact(){
        val url = Constants.CONTACTOS_URL
        if(!validatePreviewSave()) {
            // Crear el objeto JSON para el cuerpo de la solicitud
            val jsonBody = JSONObject()
            jsonBody.put("apellido_contacto", txtApellido.text.toString())
            jsonBody.put("email_contacto", txtEmail.text.toString())
            jsonBody.put("nombre_contacto", txtNombre.text.toString())
            jsonBody.put("telefono_contacto", txtTelefono.text.toString())
            jsonBody.put("id_persona", getSelectedPersonId())
            jsonBody.put("cedula_persona", "")
            jsonBody.put("id", 4)

            // Usar RequestHelper para hacer la solicitud
            requestHelper.makeJsonRequest(
                url,
                Request.Method.PUT,
                jsonBody,
                objectListener = { response -> handleResponseContactResponse(response) },
                errorListener = { error -> requestHelper.handleError(error) }
            )
        }
    }


    private fun deleteContact(selectedContactId: Int) {
        val url = "${Constants.CONTACTOS_URL}/$selectedContactId"

        // Usar RequestHelper para hacer la solicitud
        requestHelper.makeJsonRequest(
            url,
            Request.Method.DELETE,
            objectListener = { response ->
                handleResponseContactResponse(response)
            },
            errorListener = { error ->
                requestHelper.handleError(error)
            }
        )
    }



    private fun loadContacts()
    {
        val url = Constants.CONTACTOS_URL
        requestHelper.makeJsonRequest(
            url,
            Request.Method.GET,
            arrayListener = { response -> handleLoadContactsResponse(response) },
            errorListener = { error -> requestHelper.handleError(error) }
        )
    }

    private fun loadPersons() {
        val url = Constants.PERSONAS_URL
        requestHelper.makeJsonRequest(
            url,
            Request.Method.GET,
            arrayListener = { response -> handleLoadPersonsResponse(response) },
            errorListener = { error -> requestHelper.handleError(error) }
        )
    }



    private fun handleResponseContactResponse(response: JSONObject) {
        val success = response.getBoolean("success")
        val message = response.getString("message")

        if (success) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        loadContacts()
    }


    private fun handleLoadPersonsResponse(response: JSONArray) {
        try {
            // Crear una lista temporal para almacenar los objetos PersonaDto
            val personasListTemp = mutableListOf<PersonaDto>()

            for (i in 0 until response.length()) {
                val personObject = response.getJSONObject(i)

                // Extraer los datos relevantes
                val id = personObject.getInt("id")
                val nombrePersona = personObject.getString("nombre_persona")
                val apellidoPersona = personObject.getString("apellido_persona")
                val nombreCompleto = "$nombrePersona $apellidoPersona".uppercase()

                // Crear el objeto PersonaDto y agregarlo a la lista
                val persona = PersonaDto(id, nombreCompleto)
                personasListTemp.add(persona)
            }
            // Actualizar el Spinner con los datos obtenidos
            updateSpinner(personasListTemp)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSelectedPersonId(): Int {
        // Obtener la lista completa de personas guardada en el Tag
        val personaDtoList = spnn_personas.tag as List<PersonaDto>

        // Obtener el índice del elemento seleccionado
        val selectedIndex = spnn_personas.selectedItemPosition

        // Retornar el ID de la persona seleccionada
        return personaDtoList[selectedIndex].id
    }

    private fun updateSpinner(personaDto: List<PersonaDto>) {
        // Crear un adaptador con la lista de nombres completos
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            personaDto.map { it.nombreCompleto }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnn_personas.adapter = adapter
        spnn_personas.tag = personaDto
    }

    private fun handleLoadContactsResponse(response: JSONArray) {
        try {
            val contactosListTemp = mutableListOf<ContactoDto>()

            for (i in 0 until response.length()) {
                val contactObject = response.getJSONObject(i)
                val id = contactObject.getInt("id")
                val nombreContacto = contactObject.getString("nombre_contacto")
                val apellidoContacto = contactObject.getString("apellido_contacto")
                val emailContacto = contactObject.getString("email_contacto")
                val telefonoContacto = contactObject.getString("telefono_contacto")
                val idPersona = contactObject.getInt("id_persona")
                val cedulaPersona = contactObject.getString("cedula_persona")

                // Crear el objeto ContactoDto
                val contacto = ContactoDto(
                    id,
                    nombreContacto,
                    apellidoContacto,
                    emailContacto,
                    telefonoContacto,
                    idPersona,
                    cedulaPersona
                )
                contactosListTemp.add(contacto)
            }

            // Actualizar ListView con los datos cargados
            updateListView(contactosListTemp)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun updateListView(contactos: List<ContactoDto>) {
        if (contactos.isNotEmpty()) {
            // Crear un adaptador con la lista de contactos
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                contactos.map {
                            "Contacto: ${it.nombreContacto} ${it.apellidoContacto}\n" +
                            "Tel: ${it.telefonoContacto}\n" +
                            "Email: ${it.emailContacto}\n" +
                            "Contacto de: ${it.cedulaPersona}"
                }
            )

            // Asignar el adaptador al ListView
            lstPersonas.adapter = adapter

            // Verificar si el adaptador tiene datos
            if (lstPersonas.adapter.count > 0) {
                println("ListView data loaded successfully.")
            } else {
                println("ListView data is empty.")
            }
        } else {
            Toast.makeText(this, "No hay datos para mostrar en la lista.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validatePreviewSave():Boolean{
        if (txtApellido.text.isEmpty() && txtEmail.text.isEmpty() && txtNombre.text.isEmpty() && txtTelefono.text.isEmpty() && spnn_personas.selectedItemPosition == AdapterView.INVALID_POSITION) {
            showErrorMessage("Todos los campos son obligatorios")
            return true
        }
        return false;
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getContactDtoFromSelectedContact(selectedContact: String): ContactoDto {
        val parts = selectedContact.split("\n")
        val nombreApellido = parts[0].split(": ")[1]  // "Contacto: Nombre Apellido"
        val telefono = parts[1].split(": ")[1]  // "Tel: Teléfono"
        val email = parts[2].split(": ")[1]  // "Email: Email"
        val cedula = parts[3].split(": ")[1]  // "Contacto de: Cedula"

        // Aquí deberías recuperar el ID del contacto, en este ejemplo es solo un placeholder.
        val id = 4 // Este valor debe coincidir con el ID real del contacto.

        return ContactoDto(id, nombreApellido.split(" ")[0], nombreApellido.split(" ")[1], email, telefono, id, cedula)
    }


    private fun selectPersonInSpinner(idPersona: Int) {
        val personaList = spnn_personas.tag as List<PersonaDto>

        for (i in personaList.indices) {
            if (personaList[i].id == idPersona) {
                spnn_personas.setSelection(i)
                break
            }
        }
    }


}
