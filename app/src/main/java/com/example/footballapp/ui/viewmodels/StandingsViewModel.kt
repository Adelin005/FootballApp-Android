package com.example.footballapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballapp.data.RetrofitClient
import com.example.footballapp.data.TeamStanding
import com.example.footballapp.data.StandingsResponse
import com.example.footballapp.FootballApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StandingsViewModel : ViewModel() {

    private val _standings = MutableStateFlow<List<TeamStanding>>(emptyList())
    val standings: StateFlow<List<TeamStanding>> = _standings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val cacheManager = FootballApp.cacheManager

    private fun getCurrentSeason(): Int {
        // API free plan supports up to 2024 season; hardcode to avoid 2025/2026 errors
        return 2024
    }

    fun loadStandings(leagueId: String) {
        viewModelScope.launch {
            // 1. Try cache first
            val cached = cacheManager.getCachedStandings(leagueId)
            if (cached != null) {
                mapAndSetStandings(cached)
                return@launch
            }

            // 2. Fetch from API
            _isLoading.value = true
            _error.value = null
            _standings.value = emptyList()

            val season = getCurrentSeason()
            Log.d("StandingsVM", "Loading standings for leagueId=$leagueId, season=$season")

            try {
                val response = RetrofitClient.apiService.getStandings(leagueId, season)

                if (response.isSuccessful) {
                    val standingsResponseList = response.body()?.response
                    
                    if (standingsResponseList.isNullOrEmpty()) {
                        _error.value = "Nu există date pentru această ligă în sezonul $season."
                        return@launch
                    }

                    // Save to cache
                    cacheManager.saveStandings(leagueId, standingsResponseList)
                    mapAndSetStandings(standingsResponseList)

                    Log.d("StandingsVM", "Standings loaded: ${_standings.value.size} teams")

                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("StandingsVM", "API error: ${response.code()} - $errorBody")
                    _error.value = "Eroare server: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e("StandingsVM", "Exception: ${e.message}", e)
                _error.value = "Eroare conexiune: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun mapAndSetStandings(standingsResponseList: List<StandingsResponse>) {
        if (standingsResponseList.isEmpty()) return
        
        val leagueStandings = standingsResponseList.getOrNull(0)?.league?.standings
        if (leagueStandings.isNullOrEmpty()) {
            _error.value = "Clasamentul nu este disponibil."
            return
        }

        val tableEntries = leagueStandings.getOrNull(0) ?: emptyList()
        _standings.value = tableEntries.map { entry ->
            TeamStanding(
                teamName = entry.team.name,
                position = entry.rank,
                played = entry.all.played,
                win = entry.all.win,
                draw = entry.all.draw,
                loss = entry.all.lose,
                points = entry.points,
                goalsFor = entry.all.goals.goalsFor,
                goalsAgainst = entry.all.goals.goalsAgainst,
                note = entry.description
            )
        }
    }
}
