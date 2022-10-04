package com.example.commerce_test.data.service

import com.example.commerce_test.data.models.ResultDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("/v2/search/image")
    suspend fun getImage(
        @Query("query") query: String
    ): ResultDto
}