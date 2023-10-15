package com.benedetti.ssapp.model

data class Dados (
    val data: String,
    val hora: String,
    val dadoUm: String,
    val dadoDois: String,
    val botaoEditar: String? =null,
    val botaoExcluir: String? =null
)