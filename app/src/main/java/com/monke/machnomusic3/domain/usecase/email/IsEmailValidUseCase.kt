package com.monke.machnomusic3.domain.usecase.email

import javax.inject.Inject

class IsEmailValidUseCase @Inject constructor() {
    fun execute(email: String): Boolean {
        return email.isNotEmpty()
    }
}
