package com.monke.machnomusic3.domain.usecase.login

import com.monke.machnomusic3.domain.repository.RegistrationRepository
import javax.inject.Inject

class SaveLoginUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {

    fun execute(login: String) {
        registrationRepository.login = login
    }
}