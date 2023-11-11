package com.monke.machnomusic3.domain.usecase.password

import com.monke.machnomusic3.domain.repository.RegistrationRepository
import javax.inject.Inject

class SavePasswordUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
){

    fun execute(password: String) {
        registrationRepository.password = password
    }
}