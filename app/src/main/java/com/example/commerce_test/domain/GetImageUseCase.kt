package com.example.commerce_test.domain

import com.example.commerce_test.data.repository.ImageRepository
import com.example.commerce_test.di.IoDispatcher
import com.example.commerce_test.domain.model.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(query: String) = imageRepository.getImage(query)
        .map { result ->
            if(result.documents.isNotEmpty()){
                NetworkResult.Success(result.documents)
            } else {
                NetworkResult.Fail("NETWORK_ERROR")
            }
        }.flowOn(ioDispatcher)
}