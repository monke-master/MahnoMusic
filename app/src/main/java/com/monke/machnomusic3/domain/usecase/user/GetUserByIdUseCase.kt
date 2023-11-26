package com.monke.machnomusic3.domain.usecase.user

import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun execute(
        userId: String
    ) = withContext(Dispatchers.IO) {
        userRepository.getUserById(
            userId
        )
    }

}