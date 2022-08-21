package com.kerry.ubiquitiassignment

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

    private val _recordsAboveAvg = MutableLiveData<List<Record?>>()
    val recordsAboveAvg: LiveData<List<Record?>> get() = _recordsAboveAvg

    private val _recordsBelowAvg = MutableLiveData<List<Record?>>()
    val recordsBelowAvg: LiveData<List<Record?>> get() = _recordsBelowAvg

    private val _enableErrorAlert = MutableLiveData<Boolean>()
    val enableErrorAlert: LiveData<Boolean> get() = _enableErrorAlert

    fun fetchRecordList() = viewModelScope.launch {
        when (val result = repo.getAirDataResult(limit = 1000, apiKey = BuildConfig.API_KEY)) {
            is ApiResult.Success -> {
                _recordsAboveAvg.value = result.data.filter {
                    (it?.pm25 ?: 0) > (it?.pm25Avg ?: 0)
                }

                _recordsBelowAvg.value = result.data.filter {
                    (it?.pm25 ?: 0) <= (it?.pm25Avg ?: 0)
                }

                _enableErrorAlert.value = false
            }
            else -> {
                _enableErrorAlert.value = true
            }
        }

    }

}