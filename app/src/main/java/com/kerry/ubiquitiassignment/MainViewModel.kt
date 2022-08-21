package com.kerry.ubiquitiassignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerry.ubiquitiassignment.model.Record
import com.kerry.ubiquitiassignment.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MyRepository
) : ViewModel() {

    private val _goodStatusRecords = MutableLiveData<List<Record?>>()
    val goodStatusRecords: LiveData<List<Record?>> get() = _goodStatusRecords

    private val _badStatusRecords = MutableLiveData<List<Record?>>()
    val badStatusRecords: LiveData<List<Record?>> get() = _badStatusRecords

    private val _enableErrorAlert = MutableLiveData<Boolean>()
    val enableErrorAlert: LiveData<Boolean> get() = _enableErrorAlert

    fun fetchRecordList() = viewModelScope.launch {
        when (val result = repo.getAirDataResult(limit = 1000, apiKey = BuildConfig.API_KEY)) {
            is ApiResult.Success -> {
                val list: List<Record?> = result.data


                _enableErrorAlert.value = false
            }
            else -> {
                _enableErrorAlert.value = true
            }
        }

    }

}