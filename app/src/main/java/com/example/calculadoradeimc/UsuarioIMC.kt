package com.example.calculadoradeimc

import java.util.UUID

data class UsuarioIMC(
    val id: String = UUID.randomUUID().toString(),
    var peso: String,
    var altura: String,
    var imc: String
)

