
package com.example.mytusequipmentbooking.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AuthUiState(
    val uid: String? = null,
    val email: String? = null,
    val isAdmin: Boolean = false,
    val roleReady: Boolean = false,
    val isBusy: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val mutableState = MutableStateFlow(AuthUiState())
    val uiState = mutableState.asStateFlow()

    private var generation = 0

    private fun loadRole(uid: String, ticket: Int) {

        val reference = firestore.collection("users").document(uid)

        reference.get(Source.SERVER)
            .addOnSuccessListener { document ->

                if (ticket != generation || auth.currentUser?.uid != uid) {
                    return@addOnSuccessListener
                }

                if (document.exists()) {

                    mutableState.update {
                        it.copy(
                            isAdmin = document.getString("role") == "admin",
                            roleReady = true,
                            error = null
                        )
                    }

                } else {

                    // New accounts receive the normal user role.
                    // Admin roles are assigned manually in Firebase Console.
                    reference.set(mapOf("role" to "user"))
                        .addOnCompleteListener { task ->

                            if (ticket == generation &&
                                auth.currentUser?.uid == uid
                            ) {
                                mutableState.update {
                                    it.copy(
                                        isAdmin = false,
                                        roleReady = true,
                                        error = if (task.isSuccessful) null
                                        else "Account signed in; user profile could not be saved"
                                    )
                                }
                            }
                        }
                }
            }
            .addOnFailureListener {

                if (ticket == generation && auth.currentUser?.uid == uid) {
                    mutableState.update {
                        it.copy(
                            isAdmin = false,
                            roleReady = true,
                            error = "Online role check unavailable"
                        )
                    }
                }
            }
    }

    private val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->

        val ticket = ++generation
        val user = firebaseAuth.currentUser

        mutableState.update {
            it.copy(
                uid = user?.uid,
                email = user?.email,
                isAdmin = false,
                roleReady = user == null,
                error = null
            )
        }

        if (user != null) {
            loadRole(user.uid, ticket)
        }
    }

    init {
        auth.addAuthStateListener(listener)
    }

    fun login(email: String, password: String) {

        if (mutableState.value.isBusy) return

        if (email.isBlank() || password.isBlank()) {
            mutableState.update {
                it.copy(error = "Enter email and password")
            }
            return
        }

        mutableState.update {
            it.copy(isBusy = true, error = null)
        }

        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->

                mutableState.update {
                    it.copy(
                        isBusy = false,
                        error = if (task.isSuccessful) null
                        else task.exception?.localizedMessage
                            ?: "Login failed. Check credentials and connection."
                    )
                }
            }
    }

    fun register(
        email: String,
        password: String,
        confirmPassword: String
    ) {

        if (mutableState.value.isBusy) return

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            mutableState.update {
                it.copy(error = "Please complete all fields")
            }
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            mutableState.update {
                it.copy(error = "Please enter a valid email address")
            }
            return
        }

        if (password != confirmPassword) {
            mutableState.update {
                it.copy(error = "Passwords do not match")
            }
            return
        }

        mutableState.update {
            it.copy(isBusy = true, error = null)
        }

        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->

                mutableState.update {
                    it.copy(
                        isBusy = false,
                        error = if (task.isSuccessful) null
                        else task.exception?.localizedMessage
                            ?: "Registration failed. Check your password and connection."
                    )
                }
            }
    }

    fun logout() {

        if (mutableState.value.isBusy) return

        auth.signOut()
    }

    override fun onCleared() {
        ++generation
        auth.removeAuthStateListener(listener)
        super.onCleared()
    }
}
