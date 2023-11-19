package com.monke.triviamasters.domain.useCases.user


import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.RegistrationRepository
import com.monke.machnomusic3.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val registrationRepository: RegistrationRepository,
) {

    suspend fun execute(): Result<Any?> = withContext(Dispatchers.IO) {
        val user = User(
            id = registrationRepository.uid,
            email = registrationRepository.email,
            password = registrationRepository.password,
            username = registrationRepository.username,
            login = registrationRepository.login
        )
        val createRequest = registrationRepository.signUp(user)
        if (createRequest.isFailure)
            return@withContext createRequest
        return@withContext createUser(user)
    }

    private suspend fun createUser(user: User): Result<Any?> {
        val result = userRepository.createUser(user)
        if (result.isFailure)
            return result
        userRepository.updateUser(user)
        return result
    }
}