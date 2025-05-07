package pl.wmwdev.beaconservice.data.remote

import pl.wmwdev.beaconservice.data.Action
import retrofit2.http.GET

interface ApiService {
    @GET("api/elements")
    suspend fun getElements(): List<Action>
}

