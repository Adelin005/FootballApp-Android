package com.example.footballapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FootballApiService {

    @GET("countries")
    suspend fun getCountries(
        @Header("x-apisports-key") apiKey: String = "404ed9dd4684bff265bd21acef0ade61"
    ): Response<FootballApiResponse<List<Country>>>

    @GET("leagues")
    suspend fun getLeagues(
        @Query("code") countryCode: String? = null,
        @Query("season") season: Int? = null,
        @Header("x-apisports-key") apiKey: String = "404ed9dd4684bff265bd21acef0ade61"
    ): Response<FootballApiResponse<List<LeagueResponse>>>

    @GET("standings")
    suspend fun getStandings(
        @Query("league") leagueId: String,
        @Query("season") season: Int,
        @Header("x-apisports-key") apiKey: String = "404ed9dd4684bff265bd21acef0ade61"
    ): Response<FootballApiResponse<List<StandingsResponse>>>
}
