package com.wildantechnoart.frontendcodingtes.utils

import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.wildantechnoart.frontendcodingtes.R
import com.wildantechnoart.frontendcodingtes.model.Response
import com.wildantechnoart.frontendcodingtes.network.ConnectivityStatus
import retrofit2.HttpException
import java.net.SocketTimeoutException

object Constant {

    const val TOKEN_KEY_ACCESS = "token"

    fun isValidEmail(email: String?): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email.toString()).matches()
    }

    fun isTextEmpty(text: String?): Boolean {
        return TextUtils.isEmpty(text.toString()) || text == null
    }

    fun handleErrorApi(context: FragmentActivity, it: Throwable) {
        if (ConnectivityStatus.isConnected(context)) {
            when (it) {
                is HttpException -> {
                    try {
                        val gson = Gson()
                        val response = gson.fromJson(
                            it.response()?.errorBody()?.charStream(),
                            Response::class.java
                        )
                        val message = response?.message.toString()
                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.message_if_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is SocketTimeoutException -> // connection errors
                    Toast.makeText(
                        context, "Connection Timeout!",
                        Toast.LENGTH_SHORT
                    ).show()

                else -> {
                    Toast.makeText(
                        context, it.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                context, context.getString(R.string.message_if_disconnect),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun <T, VH : RecyclerView.ViewHolder> handleData(
        page: Int?,
        takeItem: Boolean,
        data: List<T>?,
        adapter: ListAdapter<T, VH>,
        recyclerView: RecyclerView?,
        textView: MaterialTextView?
    ) {
        if (page != null) {
            val oldList = adapter.currentList
            if (page == 1) {
                adapter.submitList(data)
            } else {
                val newList = oldList.toMutableList().apply {
                    addAll(data ?: mutableListOf())
                }
                adapter.submitList(newList) {
                    recyclerView?.smoothScrollToPosition(adapter.itemCount - data?.size!!)
                }
            }
        } else {
            adapter.submitList(if (takeItem) data?.take(4) else data)
        }
        val isEmpty = data?.isEmpty() ?: false
        recyclerView?.visibility = if (isEmpty) View.GONE else View.VISIBLE
        textView?.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}