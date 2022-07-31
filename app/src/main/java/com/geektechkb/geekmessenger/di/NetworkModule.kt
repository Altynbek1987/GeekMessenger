package com.geektechkb.geekmessenger.di

import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_USERS_COLLECTION_PATH
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Singleton
    @Provides
    fun provideUsersCollectionReference(firebaseFirestore: FirebaseFirestore) =
        firebaseFirestore.collection(FIREBASE_FIRESTORE_USERS_COLLECTION_PATH)
//    @Singleton
//    @Provides
//    fun provideMessagesCollectionReference(firebaseFirestore: FirebaseFirestore) =
//        firebaseFirestore.collection(
//            FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH
//        )

}