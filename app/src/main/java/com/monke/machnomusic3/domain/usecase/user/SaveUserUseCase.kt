package com.monke.machnomusic3.domain.usecase.user

import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun execute(
        newUser: User
    ) = withContext(Dispatchers.IO) {
        userRepository.updateUser(newUser)
    }
}