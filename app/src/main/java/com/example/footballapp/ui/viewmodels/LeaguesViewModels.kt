package com.example.footballapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballapp.data.Country
import com.example.footballapp.data.RetrofitClient
import com.example.footballapp.FootballApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LeaguesViewModel : ViewModel() {
    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries: StateFlow<List<Country>> = _countries

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val cacheManager = FootballApp.cacheManager

    init {
        fetchCountries()
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            // 1. Try cache
            val cached = cacheManager.getCachedCountries()
            if (cached != null) {
                _countries.value = cached
                return@launch
            }

            // 2. Fetch from API
            _isLoading.value = true
            try {
                val response = RetrofitClient.apiService.getCountries()
                if (response.isSuccessful) {
                    val data = response.body()?.response ?: emptyList()
                    _countries.value = data
                    cacheManager.saveCountries(data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
