package com.monke.machnomusic3.domain.usecase.email

import com.monke.machnomusic3.domain.repository.RegistrationRepository
import javax.inject.Inject

class SaveEmailUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {

    fun execute(email: String) {
        registrationRepository.email = email
    }

}