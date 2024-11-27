package com.example.examenes

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WebService {

    @GET("exec?spreadsheetId=1gNoiXLWeQ1SBeHoAYoPEZvXWFe4el1VNpP4DZqCYrBA&sheet=examenes")
    suspend fun obtenerTodosExamenes()
    : Response<GetResponse>

    @POST("exec")
    suspend fun agregarExamenes(
        @Body examenes: ExamenesData
    ): Response<PostResponse>
}