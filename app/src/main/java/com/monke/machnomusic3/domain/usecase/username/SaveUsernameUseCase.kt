package com.monke.machnomusic3.domain.usecase.username

import com.monke.machnomusic3.domain.repository.RegistrationRepository
import javax.inject.Inject

class SaveUsernameUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {

    fun execute(username: String) {
        registrationRepository.username = username
    }
}