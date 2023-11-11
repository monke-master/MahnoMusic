package com.monke.machnomusic3.domain.usecase.password

import javax.inject.Inject

class IsPasswordValidUseCase  @Inject constructor() {

    fun execute(password: String): Boolean {
        return password.isNotEmpty()
    }

}