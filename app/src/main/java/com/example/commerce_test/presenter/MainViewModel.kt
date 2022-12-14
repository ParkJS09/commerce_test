package com.example.commerce_test.presenter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commerce_test.data.models.Document
import com.example.commerce_test.domain.GetImageUseCase
import com.example.commerce_test.domain.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {

    private val _viewModelState = MutableStateFlow(MainViewModelState(isLoading = false))
    val uiState = _viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            _viewModelState.value.toUiState()
        )


    fun getImage(query: String) {
        Log.d("PJS","query: $query")
        viewModelScope.launch {
            _viewModelState.update { state ->
                state.copy(
                    isLoading = true,
                    noticeMsg = ""
                )
            }
            getImageUseCase(query).catch {
                showError()
            }.collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.response.map { document ->
                            _viewModelState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    oriList = state.oriList + document,
                                    imageList = (state.oriList + document).filter { item ->
                                        if (state.currentCollection == "ALL") {
                                            item.collection != "ALL"
                                        } else {
                                            item.collection == state.currentCollection
                                        }
                                    },
                                    collectionList = if (!_viewModelState.value.collectionList.contains(
                                            document.collection
                                        )
                                    ) {
                                        state.collectionList + document.collection
                                    } else {
                                        state.collectionList
                                    }
                                )
                            }
                        }
                    }
                    is NetworkResult.Empty -> {
                        showEmpty(query)
                    }
                }
            }
        }
    }

    private fun showEmpty(query: String){
        _viewModelState.update { state ->
            state.copy(
                isLoading = false,
                isError = false,
                noticeMsg = "${query}??? ?????? ????????? ????????????."
            )
        }
    }

    private fun showError() {
        _viewModelState.update { state ->
            state.copy(
                isLoading = false,
                isError = false,
                noticeMsg = "????????? ?????? ??? ????????? ?????????????????????."
            )
        }
    }

    fun setCollection(collection: String) {
        _viewModelState.update { state ->
            state.copy(
                imageList = state.oriList.filter { item ->
                    if (collection == "ALL") {
                        item.collection != collection
                    } else {
                        item.collection == collection
                    }
                },
                currentCollection = collection
            )
        }
    }
}

sealed interface MainUiState {
    val isLoading: Boolean
    val isError: Boolean
    val noticeMsg: String

    data class Empty(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val noticeMsg: String
    ) : MainUiState

    data class Image(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val noticeMsg: String,
        val collectionList: List<String>,
        val currentCollection: String,
        val oriList: List<Document>,
        val imageList: List<Document>
    ) : MainUiState
}

data class MainViewModelState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val noticeMsg: String = "",
    val collectionList: List<String> = listOf("ALL"),
    val currentCollection: String = "ALL",
    val oriList: List<Document> = emptyList(),
    val imageList: List<Document> = emptyList(),
) {
    fun toUiState(): MainUiState = if (imageList.isEmpty()) {
        MainUiState.Empty(
            isLoading = isLoading,
            isError = isError,
            noticeMsg = noticeMsg,
        )
    } else {
        MainUiState.Image(
            isLoading = isLoading,
            isError = isError,
            noticeMsg = noticeMsg,
            collectionList = collectionList,
            currentCollection = currentCollection,
            oriList = oriList,
            imageList = imageList
        )
    }
}