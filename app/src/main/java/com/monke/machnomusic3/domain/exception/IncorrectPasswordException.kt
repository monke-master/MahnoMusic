package com.monke.machnomusic3.domain.exception

class IncorrectPasswordException: Exception() {

    override val message: String?
        get() = "Incorrect password"
}