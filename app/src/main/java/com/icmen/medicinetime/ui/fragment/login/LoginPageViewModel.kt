package com.icmen.medicinetime.ui.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.icmen.medicinetime.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginPageViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _loginResult = MutableLiveData<Resource<Boolean>>()
    val loginResult: LiveData<Resource<Boolean>> = _loginResult

    fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginResult.value = Resource.Error("0")
            return
        }

        _loginResult.value = Resource.Loading()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.value = Resource.Success(true)
                } else {
                    _loginResult.value = Resource.Error( "1")
                }
            }
    }
}
