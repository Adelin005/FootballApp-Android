package com.example.footballapp.data

import com.google.gson.annotations.SerializedName

// ─── Generic Response ────────────────────────────────────────────────────────
data class FootballApiResponse<T>(
    @SerializedName("response") val response: T?
)

// ─── Countries ───────────────────────────────────────────────────────────────
data class Country(
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String?
)

// ─── Leagues ─────────────────────────────────────────────────────────────────
data class LeagueResponse(
    @SerializedName("league") val league: LeagueInfo,
    @SerializedName("country") val country: CountryInfo
)

data class LeagueInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String?
)

data class CountryInfo(
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String?
)

// ─── Standings ───────────────────────────────────────────────────────────────
data class StandingsResponse(
    @SerializedName("league") val league: LeagueStandings
)

data class LeagueStandings(
    @SerializedName("standings") val standings: List<List<StandingEntry>>
)

data class StandingEntry(
    @SerializedName("rank") val rank: Int,
    @SerializedName("team") val team: TeamInfo,
    @SerializedName("points") val points: Int,
    @SerializedName("all") val all: Stats,
    @SerializedName("description") val description: String?
)

data class TeamInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String?
)

data class Stats(
    @SerializedName("played") val played: Int,
    @SerializedName("win") val win: Int,
    @SerializedName("draw") val draw: Int,
    @SerializedName("lose") val lose: Int,
    @SerializedName("goals") val goals: Goals
)

data class Goals(
    @SerializedName("for") val goalsFor: Int,
    @SerializedName("against") val goalsAgainst: Int
)

// ─── UI Models ───────────────────────────────────────────────────────────────
data class CountryEntry(
    val code: String,
    val name: String,
    val championships: Int,
    val teams: Int = 0
)

data class LeagueEntry(
    val id: String,
    val name: String,
    val country: String,
    val has_image: Boolean,
    val important: Boolean
)

data class TeamStanding(
    val teamName: String,
    val position: Int,
    val played: Int,
    val win: Int,
    val draw: Int,
    val loss: Int,
    val points: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val note: String?
)
