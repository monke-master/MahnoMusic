package com.monke.triviamasters.di.components

import com.monke.machnomusic3.di.LoginScope
import com.monke.machnomusic3.di.module.FirebaseModule
import com.monke.machnomusic3.di.module.RegistrationModule
import com.monke.machnomusic3.ui.signInFeature.SignInFragment
import com.monke.machnomusic3.ui.signUpFeature.GenresFragment
import com.monke.machnomusic3.ui.signUpFeature.NameFragment
import com.monke.machnomusic3.ui.signUpFeature.PasswordFragment
import com.monke.machnomusic3.ui.signUpFeature.email.ConfirmEmailDialog
import com.monke.machnomusic3.ui.signUpFeature.email.EmailFragment

import dagger.Subcomponent

@Subcomponent(
    modules = [
        RegistrationModule::class
    ]
)
@LoginScope
interface LoginComponent {


    fun inject(signInFragment: SignInFragment)

    fun inject(emailFragment: EmailFragment)

    fun inject(nameFragment: NameFragment)

    fun inject(passwordFragment: PasswordFragment)

    fun inject(genresFragment: GenresFragment)

    fun inject(confirmEmailDialog: ConfirmEmailDialog)

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginComponent
    }

}