package com.bravostudio.humansinspace

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val retrofit = RetrofitHelper.getInstance().create(MainRepository::class.java)

    private val _astronautCounts = mutableStateOf(0)
    val astronautCounts: MutableState<Int>
        get() = _astronautCounts

    fun getAstronauts() {
        viewModelScope.launch {
            val result = retrofit.getAstronauts()

            if (result.isSuccessful) {
                result.body()?.let {
                    _astronautCounts.value = it.number
                }
            }
        }
    }

}