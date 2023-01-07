package com.bravostudio.humansinspace

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val retrofit = RetrofitHelper.getInstance().create(MainRepository::class.java)

    private val _astronautCount = mutableStateOf(0)
    val astronautCount: MutableState<Int>
        get() = _astronautCount

    private val _astronautList = mutableListOf<Astronaut>()
    val astronautList: MutableList<Astronaut>
        get() = _astronautList

    fun getAstronauts() {
        viewModelScope.launch {
            val result = retrofit.getAstronauts()

            if (result.isSuccessful) {
                result.body()?.let {
                    _astronautCount.value = it.number
                    _astronautList.clear()
                    _astronautList.addAll(it.people)
                }
            }
        }
    }

}