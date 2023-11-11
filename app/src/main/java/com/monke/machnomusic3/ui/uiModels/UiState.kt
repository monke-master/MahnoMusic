package com.monke.machnomusic3.ui.uiModels

import java.lang.Exception

sealed class UiState {

    object Loading : UiState()

    data class Error(
        val exception: Throwable
    ): UiState()

    data class Success(
        val data: Any? = null
    ): UiState()

}