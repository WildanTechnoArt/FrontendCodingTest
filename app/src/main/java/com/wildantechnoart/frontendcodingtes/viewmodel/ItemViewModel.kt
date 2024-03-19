package com.wildantechnoart.frontendcodingtes.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildantechnoart.frontendcodingtes.MyApp
import com.wildantechnoart.frontendcodingtes.model.ItemBody
import com.wildantechnoart.frontendcodingtes.model.ItemData
import com.wildantechnoart.frontendcodingtes.model.Response
import com.wildantechnoart.frontendcodingtes.network.RetrofitClient
import com.wildantechnoart.frontendcodingtes.repository.ItemRepository
import com.wildantechnoart.frontendcodingtes.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {

    private val repository: ItemRepository by lazy { ItemRepository(RetrofitClient.instance) }

    private val _getItemList = MutableLiveData<List<ItemData>>()
    val getItemList: LiveData<List<ItemData>> = _getItemList

    private val _successDelete = MutableLiveData<Response>()
    val successDelete: LiveData<Response> = _successDelete

    private val _successAddData = MutableLiveData<Response>()
    val successAddData: LiveData<Response> = _successAddData

    private val _successUpdate = MutableLiveData<Response>()
    val successUpdate: LiveData<Response> = _successUpdate

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getItems(context: Context, checklistId: String?) {
        viewModelScope.launch {
            try {
                val token =
                    MyApp.getInstance().readStringDataStore(context, Constant.TOKEN_KEY_ACCESS)
                repository.getItems("Bearer $token", checklistId)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getItemList.value = it.data
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun updateItems(context: Context, checklistId: String?, itemId: String) {
        viewModelScope.launch {
            try {
                val token =
                    MyApp.getInstance().readStringDataStore(context, Constant.TOKEN_KEY_ACCESS)
                repository.updateItems("Bearer $token", checklistId, itemId)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _successUpdate.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun renameItems(context: Context, checklistId: String?, itemId: String?, body: ItemBody) {
        viewModelScope.launch {
            try {
                val token =
                    MyApp.getInstance().readStringDataStore(context, Constant.TOKEN_KEY_ACCESS)
                repository.renameItems("Bearer $token", checklistId, itemId, body)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _successUpdate.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun deleteItem(context: Context, checklistId: String?, itemId: String) {
        viewModelScope.launch {
            try {
                val token =
                    MyApp.getInstance().readStringDataStore(context, Constant.TOKEN_KEY_ACCESS)
                repository.deleteItems("Bearer $token", checklistId, itemId)
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

    fun postItems(context: Context, checklistId: String?, body: ItemBody) {
        viewModelScope.launch {
            try {
                val token =
                    MyApp.getInstance().readStringDataStore(context, Constant.TOKEN_KEY_ACCESS)
                repository.postItems("Bearer $token", checklistId, body)
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