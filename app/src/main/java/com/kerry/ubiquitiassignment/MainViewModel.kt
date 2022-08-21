package com.kerry.ubiquitiassignment

import androidx.lifecycle.*
import com.kerry.ubiquitiassignment.model.Record
import com.kerry.ubiquitiassignment.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

    private var _allRecords: List<Record?> = listOf()
    private val _keyword = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    val searchedRecords = _keyword
        .debounce(500)
        .map { keyword ->
            if (keyword.isEmpty()) {
                _allRecords
            } else {
                _allRecords.filter { record ->
                    record?.siteName?.contains(keyword) == true || record?.county?.contains(keyword) == true
                }
            }
        }
        .asLiveData()

    fun fetchRecordList() = viewModelScope.launch {
        when (val result = repo.getAirDataResult(limit = 1000, apiKey = BuildConfig.API_KEY)) {
            is ApiResult.Success -> {
                _allRecords = result.data

                _recordsAboveAvg.value = _allRecords.filter {
                    (it?.pm25 ?: 0) > (it?.pm25Avg ?: 0)
                }

                _recordsBelowAvg.value = _allRecords.filter {
                    (it?.pm25 ?: 0) <= (it?.pm25Avg ?: 0)
                }

                _enableErrorAlert.value = false
            }
            else -> {
                _enableErrorAlert.value = true
            }
        }

    }

    fun onTextChanged(keyword: String) {
        _keyword.value = keyword
    }

}