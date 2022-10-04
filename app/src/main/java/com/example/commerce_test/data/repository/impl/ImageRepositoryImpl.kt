package com.example.commerce_test.data.repository.impl

import com.example.commerce_test.data.models.ResultDto
import com.example.commerce_test.data.repository.ImageRepository
import com.example.commerce_test.data.service.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
) : ImageRepository {
    override fun getImage(query: String): Flow<ResultDto> = flow {
        emit(
            networkService.getImage(query)
        )
    }
}