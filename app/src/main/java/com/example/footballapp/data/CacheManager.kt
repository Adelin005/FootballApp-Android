package com.example.footballapp.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class CacheManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("football_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    private fun todayStr(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    // ─── Countries Cache ────────────────────────────────────────
    fun getCachedCountries(): List<Country>? {
        val date = prefs.getString("countries_date", "")
        if (date == todayStr()) {
            val json = prefs.getString("countries_data", null)
            if (json != null) {
                val type = object : TypeToken<List<Country>>() {}.type
                return gson.fromJson(json, type)
            }
        }
        return null
    }

    fun saveCountries(countries: List<Country>) {
        prefs.edit()
            .putString("countries_date", todayStr())
            .putString("countries_data", gson.toJson(countries))
            .apply()
    }

    // ─── Leagues Cache (Per Country) ────────────────────────────
    fun getCachedLeagues(countryCode: String): List<LeagueResponse>? {
        val date = prefs.getString("leagues_date_$countryCode", "")
        if (date == todayStr()) {
            val json = prefs.getString("leagues_data_$countryCode", null)
            if (json != null) {
                val type = object : TypeToken<List<LeagueResponse>>() {}.type
                return gson.fromJson(json, type)
            }
        }
        return null
    }

    fun saveLeagues(countryCode: String, leagues: List<LeagueResponse>) {
        prefs.edit()
            .putString("leagues_date_$countryCode", todayStr())
            .putString("leagues_data_$countryCode", gson.toJson(leagues))
            .apply()
    }

    // ─── Standings Cache (Per League) ───────────────────────────
    fun getCachedStandings(leagueId: String): List<StandingsResponse>? {
        val date = prefs.getString("standings_date_$leagueId", "")
        if (date == todayStr()) {
            val json = prefs.getString("standings_data_$leagueId", null)
            if (json != null) {
                val type = object : TypeToken<List<StandingsResponse>>() {}.type
                return gson.fromJson(json, type)
            }
        }
        return null
    }

    fun saveStandings(leagueId: String, standings: List<StandingsResponse>) {
        prefs.edit()
            .putString("standings_date_$leagueId", todayStr())
            .putString("standings_data_$leagueId", gson.toJson(standings))
            .apply()
    }
}
