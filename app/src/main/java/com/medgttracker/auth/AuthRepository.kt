package com.medgttracker.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sessionPreferences: SessionPreferences,
) {
    val currentUserId: String? get() = firebaseAuth.currentUser?.uid

    suspend fun signInWithEmail(email: String, password: String): String {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val uid = requireNotNull(result.user?.uid)
        sessionPreferences.markLoggedIn(uid)
        return uid
    }

    suspend fun createEmailAccount(email: String, password: String): String {
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val uid = requireNotNull(result.user?.uid)
        sessionPreferences.markLoggedIn(uid)
        return uid
    }

    fun startPhoneLogin(
        phoneNumber: String,
        activity: Activity,
        onVerificationId: (String) -> Unit,
        onAutoVerified: (String) -> Unit,
        onError: (FirebaseException) -> Unit,
    ) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                firebaseAuth.signInWithCredential(credential).addOnSuccessListener { result ->
                    result.user?.uid?.let { uid ->
                        CoroutineScope(Dispatchers.Main).launch {
                            sessionPreferences.markLoggedIn(uid)
                            onAutoVerified(uid)
                        }
                    }
                }
            }

            override fun onVerificationFailed(exception: FirebaseException) = onError(exception)
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) = onVerificationId(verificationId)
        }
        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build(),
        )
    }

    suspend fun verifyOtp(verificationId: String, otp: String): String {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        val result = firebaseAuth.signInWithCredential(credential).await()
        val uid = requireNotNull(result.user?.uid)
        sessionPreferences.markLoggedIn(uid)
        return uid
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
        sessionPreferences.clear()
    }
}
