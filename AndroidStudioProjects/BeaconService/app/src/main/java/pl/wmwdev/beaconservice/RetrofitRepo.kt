package pl.wmwdev.beaconservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRepo {
    private const val BASE_URL = "https://actioncreatorapi.onrender.com/"

    val api : ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

class ActionRepo {
    suspend fun fetchElements(): ApiResponse<List<Action>> {
        return try {
            val data = RetrofitRepo.api.getElements()
            ApiResponse.Success(data)
        } catch(e: Exception) {
            ApiResponse.Error(e.message ?: "Api Error")
        }
    }
}