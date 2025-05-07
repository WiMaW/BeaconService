package pl.wmwdev.beaconservice

import retrofit2.http.GET

interface ApiService {
    @GET("api/elements")
    suspend fun getElements(): List<Action>
}

