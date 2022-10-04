package com.example.commerce_test.data.repository

import com.example.commerce_test.data.models.ResultDto
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImage(query: String): Flow<ResultDto>
}