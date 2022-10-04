package com.example.commerce_test.domain

import android.util.Log
import com.example.commerce_test.data.repository.ImageRepository
import com.example.commerce_test.di.IoDispatcher
import com.example.commerce_test.domain.model.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.http.Query
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    operator fun invoke(query: String) = imageRepository.getImage(query)
        .map {
            Log.d("TEST", it.toString())
            if(it.meta.total_count != 0) {
                NetworkResult.Success(it)
            } else {
                NetworkResult.Fail(it)
            }
        }.flowOn(ioDispatcher)
}