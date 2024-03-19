package com.wildantechnoart.frontendcodingtes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildantechnoart.frontendcodingtes.MyApp
import com.wildantechnoart.frontendcodingtes.model.LoginBody
import com.wildantechnoart.frontendcodingtes.model.LoginResponse
import com.wildantechnoart.frontendcodingtes.model.RegisterBody
import com.wildantechnoart.frontendcodingtes.model.Response
import com.wildantechnoart.frontendcodingtes.network.RetrofitClient
import com.wildantechnoart.frontendcodingtes.repository.AuthRepository
import com.wildantechnoart.frontendcodingtes.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository: AuthRepository by lazy { AuthRepository(RetrofitClient.instance) }

    private val _loginSuccess = MutableLiveData<LoginResponse>()
    val loginSuccess: LiveData<LoginResponse> = _loginSuccess

    private val _registerSuccess = MutableLiveData<Response>()
    val registerSuccess: LiveData<Response> = _registerSuccess

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun loginUser(context: Context, data: LoginBody) {
        viewModelScope.launch {
            try {
                repository.login(data)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        MyApp.getInstance()
                            .saveStringDataStore(
                                context,
                                Constant.TOKEN_KEY_ACCESS,
                                it.data?.token ?: "-"
                            )

                        _loginSuccess.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun registerUser(context: Context, data: RegisterBody) {
        viewModelScope.launch {
            try {
                repository.register(data)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _registerSuccess.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    private fun errorHandle(it: Throwable) {
        _loading.postValue(false)
        _error.postValue(it)
    }
}