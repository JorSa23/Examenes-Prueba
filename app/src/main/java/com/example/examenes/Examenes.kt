package com.example.examenes

data class Examenes(
    val ID: String,
    val MATERIA: String,
    val DIA: String,
    val FECHA: String,
    val HORA: String,

)

data class ExamenesData(
    val spreadsheet_id: String,
    val sheet: String,
    val rows: List<List<String>>
)
