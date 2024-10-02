package com.icmen.medicinetime.ui.fragment.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.icmen.medicinetime.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterPageViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _registrationResult = MutableLiveData<Resource<FirebaseUser>>()
    val registrationResult: LiveData<Resource<FirebaseUser>> = _registrationResult

    private val _progressVisibility = MutableLiveData<Boolean>()
    val progressVisibility: LiveData<Boolean> = _progressVisibility

    fun registerUser(name: String, surname: String, email: String, password: String, birthday: String) {
        if (name.isEmpty() || surname.isEmpty() || birthday.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _registrationResult.value = Resource.Error("0")
            return
        }

        if (!isEmailValid(email)) {
            _registrationResult.value = Resource.Error("5")
            return
        }

        if (password.length < 6) {
            _registrationResult.value = Resource.Error("1")
            return
        }

        _progressVisibility.value = true
        _registrationResult.value = Resource.Loading()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUserInfo(it.uid, name, surname, email, birthday)
                    }
                } else {
                    _progressVisibility.value = false
                    if (task.exception is FirebaseAuthException) {
                        val errorCode = (task.exception as FirebaseAuthException).errorCode
                        when (errorCode) {
                            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                _registrationResult.value = Resource.Error("2")
                            }
                            else -> {
                                _registrationResult.value = Resource.Error("3")
                            }
                        }
                    } else {
                        _registrationResult.value = Resource.Error("4") //Unknown error occurred
                    }
                }
            }

    }

    private fun saveUserInfo(userId: String, name: String, surname: String, email: String, birthday: String) {
        val userInfo = hashMapOf(
            "userId" to userId,
            "name" to name,
            "surname" to surname,
            "email" to email,
            "birthday" to birthday
        )
        db.collection("users").document(userId).set(userInfo)
            .addOnSuccessListener {
                _progressVisibility.value = false
                _registrationResult.value = Resource.Success(auth.currentUser!!)
            }
            .addOnFailureListener { e ->
                _progressVisibility.value = false
                _registrationResult.value = Resource.Error(e.message ?: "Failed to save user info")
            }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }
}
