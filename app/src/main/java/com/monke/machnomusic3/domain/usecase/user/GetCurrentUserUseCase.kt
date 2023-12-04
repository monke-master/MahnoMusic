package com.monke.machnomusic3.domain.usecase.user

import com.monke.machnomusic3.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    fun execute() = userRepository.user

}

