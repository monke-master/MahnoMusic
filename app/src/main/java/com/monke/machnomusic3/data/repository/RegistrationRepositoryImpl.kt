package com.monke.machnomusic3.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.monke.machnomusic3.di.LoginScope
import com.monke.machnomusic3.domain.model.User
import com.monke.machnomusic3.domain.repository.RegistrationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

@LoginScope
class RegistrationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): RegistrationRepository {

    private val emailConfirmed = MutableStateFlow<Boolean>(false)
    override var email : String = ""
    override var password: String = ""
    override var uid: String = ""
    private lateinit var rawPassword: String

    init {
        Log.d("RegistrationRepository", "init block")

    }

    override suspend fun sendConfirmationLetter(): Result<Any?> {
        Log.d("RegistrationRepository", "email has been sent")
        try {
            // Создает пробную запись, чтобы потом отправить письмо с подтверждением
            rawPassword = UUID.randomUUID().toString()
            val result = firebaseAuth.createUserWithEmailAndPassword(
                email,
                rawPassword
            ).await()
            result?.user?.sendEmailVerification()?.await()
            uid = result.user?.uid ?: uid
            return Result.success(null)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }


    override fun getConfirmationStatus(): StateFlow<Boolean> = emailConfirmed

    override suspend fun signUp(user: User): Result<String?> {
        try {
            firebaseAuth.currentUser?.updatePassword(user.password)?.await()
            return Result.success(firebaseAuth.currentUser?.uid)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }


    // Раз в 5 секунд проверяет, подтвержден ли email пользователя
    override suspend fun startCheckingConfirmationStatus(): Result<Any?> {
        while (true) {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, rawPassword).await()
                val user = firebaseAuth.currentUser
                if (user?.isEmailVerified != false) {
                    emailConfirmed.value = true
                    return Result.success(null)
                }
                delay(5000)
            } catch (exception: Exception) {
                exception.printStackTrace()
                return Result.failure(exception)
            }
        }
    }


}