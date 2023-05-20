package com.aleet.chattleroyale.signUp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpModel(
    var email: String,
    var password: String,
    var confirmPassword: String
): Parcelable