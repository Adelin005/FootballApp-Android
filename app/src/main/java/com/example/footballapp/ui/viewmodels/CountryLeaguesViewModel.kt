package com.example.footballapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballapp.data.LeagueEntry
import com.example.footballapp.data.RetrofitClient
import com.example.footballapp.FootballApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountryLeaguesViewModel : ViewModel() {

    private val _leagues = MutableStateFlow<List<LeagueEntry>>(emptyList())
    val leagues: StateFlow<List<LeagueEntry>> = _leagues

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val cacheManager = FootballApp.cacheManager

    private val majorNames = setOf(
        "Premier League", "La Liga", "Serie A", "Bundesliga", "Ligue 1",
        "Eredivisie", "Primeira Liga", "Super Lig", "Scottish Premiership",
        "Jupiler Pro League", "Championship", "Serie B", "2. Bundesliga",
        "Liga NOS", "Premier Liga", "Superliga", "Primera División",
        "Premiership", "MLS", "Liga MX", "Brasileirao Serie A"
    )

    fun loadLeagues(countryCode: String) {
        viewModelScope.launch {
            // 1. Try cache first
            val cachedResponse = cacheManager.getCachedLeagues(countryCode)
            if (cachedResponse != null) {
                _leagues.value = cachedResponse.map { entry ->
                    LeagueEntry(
                        id = entry.league.id.toString(),
                        name = entry.league.name,
                        country = entry.country.code ?: "",
                        has_image = !entry.league.logo.isNullOrEmpty(),
                        important = majorNames.contains(entry.league.name)
                    )
                }
                return@launch
            }

            // 2. Fetch from API
            _isLoading.value = true
            _error.value = null
            _leagues.value = emptyList()

            Log.d("LeaguesVM", "Loading leagues for country=$countryCode")

            try {
                val response = RetrofitClient.apiService.getLeagues(countryCode)
                Log.d("LeaguesVM", "HTTP ${response.code()}")

                if (response.isSuccessful) {
                    val rawLeagues = response.body()?.response ?: emptyList()
                    cacheManager.saveLeagues(countryCode, rawLeagues) // Save raw to cache

                    val data = rawLeagues.map { entry ->
                        LeagueEntry(
                            id = entry.league.id.toString(),
                            name = entry.league.name,
                            country = entry.country.code ?: "",
                            has_image = !entry.league.logo.isNullOrEmpty(),
                            important = majorNames.contains(entry.league.name)
                        )
                    }
                    Log.d("LeaguesVM", "Leagues found: ${data.size}")
                    _leagues.value = data
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LeaguesVM", "Error: ${response.code()} - $errorBody")
                    _error.value = "Eroare: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("LeaguesVM", "Exception: ${e.message}", e)
                _error.value = "Nu s-au putut încărca ligile."
            } finally {
                _isLoading.value = false
            }
        }
    }
}
