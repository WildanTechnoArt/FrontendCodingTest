package com.wildantechnoart.frontendcodingtes.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildantechnoart.frontendcodingtes.MyApp
import com.wildantechnoart.frontendcodingtes.model.AddChecklistBody
import com.wildantechnoart.frontendcodingtes.model.ItemData
import com.wildantechnoart.frontendcodingtes.model.Response
import com.wildantechnoart.frontendcodingtes.network.RetrofitClient
import com.wildantechnoart.frontendcodingtes.repository.ChecklistRepository
import com.wildantechnoart.frontendcodingtes.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ChecklistViewModel : ViewModel() {

    private val repository: ChecklistRepository by lazy { ChecklistRepository(RetrofitClient.instance) }

    private val _getChecklist = MutableLiveData<List<ItemData>>()
    val getChecklist: LiveData<List<ItemData>> = _getChecklist

    private val _successDelete = MutableLiveData<Response>()
    val successDelete: LiveData<Response> = _successDelete

    private val _successAddData = MutableLiveData<Response>()
    val successAddData: LiveData<Response> = _successAddData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getChecklist(context: Context) {
        viewModelScope.launch {
            try {
                val token =
                    MyApp.getInstance().readStringDataStore(context, Constant.TOKEN_KEY_ACCESS)
                repository.getChecklist("Bearer $token")
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getChecklist.value = it.data
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun deleteChecklist(context: Context, id: String?) {
        viewModelScope.launch {
            try {
                val token =
                    MyApp.getInstance().readStringDataStore(context, Constant.TOKEN_KEY_ACCESS)
                repository.deleteChecklist("Bearer $token", id)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _successDelete.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun addChecklist(context: Context, body: AddChecklistBody?) {
        viewModelScope.launch {
            try {
                val token =
                    MyApp.getInstance().readStringDataStore(context, Constant.TOKEN_KEY_ACCESS)
                repository.addChecklist("Bearer $token", body)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _successAddData.value = it
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