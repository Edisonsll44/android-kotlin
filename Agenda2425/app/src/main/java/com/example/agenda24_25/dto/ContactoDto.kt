package com.example.agenda24_25.dto

data class ContactoDto(
    val id: Int,
    val nombreContacto: String,
    val apellidoContacto: String,
    val emailContacto: String,
    val telefonoContacto: String,
    val idPersona: Int,
    val cedulaPersona: String
)