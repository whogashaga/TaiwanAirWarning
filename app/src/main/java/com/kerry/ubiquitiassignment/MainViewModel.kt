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

    private val _recordsAbovePm30 = MutableLiveData<List<Record?>>()
    val recordsAbovePm30: LiveData<List<Record?>> get() = _recordsAbovePm30

    private val _recordsBelowPm30 = MutableLiveData<List<Record?>>()
    val recordsBelowPm30: LiveData<List<Record?>> get() = _recordsBelowPm30

    private val _enableErrorAlert = MutableLiveData<Boolean>()
    val enableErrorAlert: LiveData<Boolean> get() = _enableErrorAlert

    fun fetchRecordList() = viewModelScope.launch {
        when (val result = repo.getAirDataResult(limit = 1000, apiKey = BuildConfig.API_KEY)) {
            is ApiResult.Success -> {
                _recordsAbovePm30.value = result.data.filter {
                    (it?.pmTwoPointFive?.toIntOrNull() ?: 0) > 30
                }

                _recordsBelowPm30.value = result.data.filter {
                    (it?.pmTwoPointFive?.toIntOrNull() ?: 0) <= 30
                }

                _enableErrorAlert.value = false
            }
            else -> {
                _enableErrorAlert.value = true
            }
        }

    }

}