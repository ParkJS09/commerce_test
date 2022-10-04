package com.example.commerce_test.di

import com.example.commerce_test.data.repository.ImageRepository
import com.example.commerce_test.data.repository.impl.ImageRepositoryImpl
import com.example.commerce_test.data.service.NetworkService
import com.example.commerce_test.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@Module
@InstallIn(ViewModelComponent::class)
abstract class ApiModule {
    @Binds
    @ViewModelScoped
    abstract fun bindImageRepository(imageRepositoryImpl: ImageRepositoryImpl): ImageRepository

    companion object {
        private const val KAKAO_BASE_URL = "https://dapi.kakao.com"

        @Provides
        @ViewModelScoped
        fun provideNetworkService(): NetworkService =
            Retrofit.Builder()
                .baseUrl(KAKAO_BASE_URL)
                .client(provideOkHttpClient(AppInterceptor()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NetworkService::class.java)


        private fun provideOkHttpClient(interceptor: AppInterceptor): OkHttpClient
                = OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }

        class AppInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
                val newRequest = request().newBuilder()
                    .addHeader("Authorization", Constants.API_KEY)
                    .build()
                proceed(newRequest)
            }
        }
    }



}