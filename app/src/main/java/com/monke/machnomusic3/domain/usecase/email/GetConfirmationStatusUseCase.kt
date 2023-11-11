package com.monke.machnomusic3.domain.usecase.email

import com.monke.machnomusic3.domain.repository.RegistrationRepository
import javax.inject.Inject

class GetConfirmationStatusUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {

    fun execute() = registrationRepository.getConfirmationStatus()
}