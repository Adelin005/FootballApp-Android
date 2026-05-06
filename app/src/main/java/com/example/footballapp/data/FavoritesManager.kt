package com.example.footballapp.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class FavoriteTeam(
    val teamId: String,
    val teamName: String,
    val leagueId: String,
    val leagueName: String,
    val teamStanding: TeamStanding
)

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _favorites = MutableStateFlow<List<FavoriteTeam>>(emptyList())
    val favorites: StateFlow<List<FavoriteTeam>> = _favorites

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        val json = prefs.getString("favorites_list", "[]")
        val type = object : TypeToken<List<FavoriteTeam>>() {}.type
        val list: List<FavoriteTeam> = gson.fromJson(json, type) ?: emptyList()
        _favorites.value = list
    }

    fun toggleFavorite(leagueId: String, leagueName: String, teamStanding: TeamStanding) {
        val currentList = _favorites.value.toMutableList()
        val index = currentList.indexOfFirst { it.teamName == teamStanding.teamName }

        if (index >= 0) {
            currentList.removeAt(index)
        } else {
            currentList.add(
                FavoriteTeam(
                    teamId = teamStanding.teamName, // Se folosește numele ca id
                    teamName = teamStanding.teamName,
                    leagueId = leagueId,
                    leagueName = leagueName,
                    teamStanding = teamStanding
                )
            )
        }

        saveFavorites(currentList)
    }

    private fun saveFavorites(list: List<FavoriteTeam>) {
        val json = gson.toJson(list)
        prefs.edit().putString("favorites_list", json).apply()
        _favorites.value = list
    }

    fun isFavorite(teamName: String): Boolean {
        return _favorites.value.any { it.teamName == teamName }
    }
}
