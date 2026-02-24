package com.optic.pramosreservasappz.presentation.screens.auth.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.optic.pramosreservasappz.domain.model.auth.AuthResponse
import com.optic.pramosreservasappz.domain.model.auth.LoginSendCodeResponse
import com.optic.pramosreservasappz.domain.useCase.auth.AuthUseCase
import com.optic.pramosreservasappz.domain.useCase.external.ExternalUseCase

import com.optic.pramosreservasappz.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val externalUseCase: ExternalUseCase
): ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    var errorMessage by mutableStateOf("")

    // LOGIN RESPONSE
    var loginResponse by mutableStateOf<Resource<AuthResponse>?>(null)
        private set

    var loginSendCodeResponse by mutableStateOf<Resource<LoginSendCodeResponse>?>(null)
        private set

    private val _sendCodeSuccess = mutableStateOf(false)
    val sendCodeSuccess: State<Boolean> = _sendCodeSuccess

    // ✅ Variable observable del estado de sesión
    private var _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // el estado del send code
    private val _sendCodeState = MutableStateFlow<Resource<LoginSendCodeResponse>>(Resource.Idle)
    var sendCodeState: StateFlow<Resource<LoginSendCodeResponse>> = _sendCodeState


    init {
        getSessionData()
    }

    fun getSessionData() = viewModelScope.launch {
        authUseCase.getSessionData().collect() { data ->
            Log.d("LoginViewModel", "Data: ${data.toJson()}")
            if (!data.token.isNullOrBlank()) {
                loginResponse = Resource.Success(data)
                _isLoggedIn.value = true // ✅ Usuario logueado
            } else {
                _isLoggedIn.value = false
            }
        }
    }

    fun saveSession(authResponse: AuthResponse) = viewModelScope.launch {
        authUseCase.saveSession(authResponse)
        _isLoggedIn.value = true // ✅ Actualiza el estado
    }



    fun login() = viewModelScope.launch {
        Log.d("LoginViewModel", "Email: ${state.email}, Password: ${state.password}")
        if (isValidForm()) {
            Log.d("LoginViewModel", "Email: ${state.email}, Password: ${state.password}")
            loginResponse = Resource.Loading // ESPERANDO


            try {
                val result = authUseCase.login(state.email, state.password)
                loginResponse = result
                if (result is Resource.Success) {
                    _isLoggedIn.value = true
                    _navigateToHome.value = true
                    saveSession(result.data)

                    // guarde si tiene prodes en cache
                   // teamUseCase.syncCacheUC()
                } else {
                    _isLoggedIn.value = false
                }
            } catch (e: Exception) {
                loginResponse = Resource.Failure(e.message ?: "Error desconocido al iniciar con login basic")
                _isLoggedIn.value = false
            }

        } else {
            _isLoggedIn.value = false
        }

    }

    // login de google

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            loginResponse = Resource.Loading
            Log.d("loginWithGoogle", "loading")
            try {
                val result = externalUseCase.login(idToken)
                loginResponse = result
                if (result is Resource.Success) {
                    Log.d("loginWithGoogle", "result sucess: ${result}")
                    saveSession(result.data)
                } else {
                    Log.d("loginWithGoogle", "result NO success: ${result}")
                    _isLoggedIn.value = false
                }
            } catch (e: Exception) {
                Log.d("loginWithGoogle", "Error general en login con google api: ${e.message }")
                loginResponse = Resource.Failure(e.message ?: "Error desconocido al iniciar con Google")
                _isLoggedIn.value = false
            }
        }
    }

    fun onEmailInput(email: String) {
        state = state.copy(email = email)
    }

    fun onPasswordInput(password: String) {
        state = state.copy(password = password)
    }

    fun onCodeInput(code: String) {
        state = state.copy(code = code)
    }


    fun isValidForm(): Boolean  {

        if (state.email == null || state.email == "") {
            errorMessage = "Debe ingresar en email"
            return false
        }
        if (state.email.length > 100) {
            errorMessage = "El email no es valido (1)"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            errorMessage = "El email no es valido (2)"
            return false
        }

        /*
        else if (state.password.length < 6) {
            errorMessage = "La contraseña debe tener al menos 6 caracteres"
            return false
        }

         */
        return true
    }

    fun isValidSendCode(): Boolean  {

        if (state.code == null || state.code == "") {
            errorMessage = "Debe ingresar el codigo"
            return false
        }
        if (state.code.length > 6 || state.code.length < 6) {
            errorMessage = "El codigo debe ser de 6 digitos"
            return false
        }

        return true
    }


    // ✅ NUEVA FUNCIÓN: permite comprobar login de forma imperativa
    fun isLoggedInNow(): Boolean = _isLoggedIn.value

    //Password Less
    //LOogin enviando codigo de acceso al email ingresado

    fun loginSendCode() = viewModelScope.launch {

        try {
            if (isValidForm()) {
                val result = authUseCase.loginSendCodeUC(state.email) // tu función real
                _sendCodeState.value = result
                Log.d("LoginViewModel", "result: ${result}")
                if (result is Resource.Success) {
                    _sendCodeSuccess.value = true
                } else {
                    errorMessage = "Error al enviar el código. Intenta nuevamente."
                }
            } else {
                _isLoggedIn.value = false
            }
        } catch (e: Exception) {
            errorMessage = "Error al enviar el código: ${e.message}"
        }
    }

    fun loginPless(email:String) = viewModelScope.launch {
        if ( isValidSendCode() ) {

            Log.d("LoginViewModel", "Email: ${email}, Code: ${state.code}")
            loginResponse = Resource.Loading // ESPERANDO
            val result = authUseCase.loginPlessUC(email, state.code) // RETORNA UNA RESPUESTA
            Log.d("LoginViewModel", "result: ${result}")
            loginResponse = result // EXITOSA / ERROR
            if (result is Resource.Success) {
                _isLoggedIn.value = true
                _navigateToHome.value = true
                saveSession(result.data)

                // guarde si tiene prodes en cache
               // teamUseCase.syncCacheUC()


            } else {
                _isLoggedIn.value = false
                errorMessage = "Error al validar el codigo. Intenta nuevamente mas tarde.${result}"
            }

        }

    }

    fun resetSendCodeSuccess() {
        _sendCodeSuccess.value = false
    }

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome

}