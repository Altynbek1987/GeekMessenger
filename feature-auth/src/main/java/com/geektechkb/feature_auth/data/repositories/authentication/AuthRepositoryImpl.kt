package com.geektechkb.feature_auth.data.repositories.authentication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.geektechkb.common.constants.Constants.FIREBASE_CLOUD_STORAGE_PROFILE_IMAGES_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_USERS_COLLECTION_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_USER_NAME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PHONE_NUMBER_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PROFILE_IMAGE_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_SURNAME_KEY
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.core.typealiases.NotAnActualActivity
import com.geektechkb.core.typealiases.NotAnActualCallbacks
import com.geektechkb.core.typealiases.NotAnActualFirebaseAuth
import com.geektechkb.core.typealiases.NotAnActualUri
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authorizationPreferences: AuthorizePreferences,
    firebaseFirestore: FirebaseFirestore,
    cloudStorage: FirebaseStorage,

    ) : BaseRepository(), AuthRepository {
    private val usersRef = firebaseFirestore.collection(FIREBASE_FIRESTORE_USERS_COLLECTION_PATH)
    private val cloudStorageRef = cloudStorage.reference

    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    override fun isUserAuthenticated(): Boolean {
        authorizationPreferences.isAuthorized = firebaseAuth.currentUser != null
        return authorizationPreferences.isAuthorized
    }

    override fun provideAuthenticationCallbacks(
        authenticationSucceeded: ((() -> Unit))?,
        authInvalidCredentialsError: ((() -> Unit))?,
        tooManyRequestsError: ((() -> Unit))?
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                authenticationSucceeded?.invoke()
            }

            override fun onVerificationFailed(e: FirebaseException) {

                if (e is FirebaseAuthInvalidCredentialsException) {
                    authInvalidCredentialsError?.invoke()
                } else if (e is FirebaseTooManyRequestsException) {
                    tooManyRequestsError?.invoke()
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                authorizationPreferences.verificationId = verificationId
                forceResendingToken = token
            }

        }
        return callbacks
    }

    override fun startPhoneNumberVerification(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks
    ) {
        notAnActualFirebaseAuth as FirebaseAuth
        notAnActualFirebaseAuth.setLanguageCode("ru")
        val options = PhoneAuthOptions.newBuilder(notAnActualFirebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(notAnActualActivity as AppCompatActivity)
            .setCallbacks(
                notAnActualCallbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks
            )
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun provideResendingToken() = forceResendingToken
    override suspend fun authenticateUser(
        phoneNumber: String,
        name: String,
        surname: String,
        profileImage: NotAnActualUri?,
        imageFileName: String
    ) {
        addDocument(
            usersRef, hashMapOf(
                FIREBASE_USER_PHONE_NUMBER_KEY to phoneNumber,
                FIREBASE_USER_NAME_KEY to name,
                FIREBASE_USER_SURNAME_KEY to surname,
                FIREBASE_USER_PROFILE_IMAGE_KEY to uploadUncompressedImageToCloudStorage(
                    cloudStorageRef,
                    profileImage as Uri?,
                    FIREBASE_CLOUD_STORAGE_PROFILE_IMAGES_PATH,
                    imageFileName
                ).toString()
            )
        )
    }
}