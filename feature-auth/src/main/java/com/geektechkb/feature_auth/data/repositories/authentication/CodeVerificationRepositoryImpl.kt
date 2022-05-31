package com.geektechkb.feature_auth.data.repositories.authentication

import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.domain.repositories.CodeVerificationRepository
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject


class CodeVerificationRepositoryImpl @Inject constructor(
    private val authorizationPreferences: AuthorizePreferences
) : BaseRepository(), CodeVerificationRepository {
    fun verifyPhoneNumberWithCode(
        verificationId: String?,
        code: String,
    ): PhoneAuthCredential {
        return getPhoneAuthCredential(verificationId!!, code)

    }

//    fun cast(){
//        val nonAnActualActivity = NonAnActualActivity ()
//        nonAnActualActivity as AppCompatActivity
//    }

    override fun getVerificationId() = authorizationPreferences.verificationId

    private fun getPhoneAuthCredential(verificationId: String?, code: String) =
        PhoneAuthProvider.getCredential(verificationId.toString(), code)
}