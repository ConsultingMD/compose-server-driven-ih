package com.serverdriven.app.ui.home

import android.content.Context
import android.content.res.AssetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.serverdriven.app.data.models.CustomView
import com.serverdriven.app.utils.readAssetsFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _dataState = MutableStateFlow(HomeUIState())
    val dataState: StateFlow<HomeUIState> = _dataState

    private val _fileName = MutableStateFlow("data_server_1.json")
    val fileName: StateFlow<String> = _fileName

    fun getData(context: Context) {
        _dataState.value = _dataState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonData = context.assets.readAssetsFile(_fileName.value)
                val data = Gson().fromJson(jsonData, CustomView::class.java)
                delay(1000)
                _dataState.value = _dataState.value.copy(
                    isLoading = false,
                    child = listOf(data)
                )
            }catch (e: Exception) {
                _dataState.value = _dataState.value.copy(isLoading = false, error = e.localizedMessage)
            }
        }
    }

    fun changeNextDesign(context: Context) {
        when (_fileName.value) {
            "data_server_1.json" -> {
                _fileName.value = "data_server_2.json"
            }
            "data_server_2.json" -> {
                _fileName.value = "data_server_3.json"
            }
            "data_server_3.json" -> {
                _fileName.value = "data_server_4.json"
            }
            else -> {
                _fileName.value = "data_server_1.json"
            }
        }
        getData(context)
    }

    data class HomeUIState(
        val child: List<CustomView> = emptyList(),
        val isLoading: Boolean = true,
        val error: String? = null
    )
}






