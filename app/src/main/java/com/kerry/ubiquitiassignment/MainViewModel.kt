package com.kerry.ubiquitiassignment

import androidx.lifecycle.ViewModel
import com.kerry.ubiquitiassignment.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {


}