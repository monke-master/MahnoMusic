package com.monke.machnomusic3.domain.repository

import com.monke.machnomusic3.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface RegistrationRepository {

    var email: String

    var password: String

    var username: String

    var login: String

    var uid: String

    suspend fun sendConfirmationLetter(): Result<Any?>


    fun getConfirmationStatus(): StateFlow<Boolean>


    suspend fun signUp(user: User): Result<String?>


    suspend fun startCheckingConfirmationStatus(): Result<Any?>

}