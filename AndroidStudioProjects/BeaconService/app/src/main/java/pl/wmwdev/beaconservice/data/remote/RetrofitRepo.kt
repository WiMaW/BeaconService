package pl.wmwdev.beaconservice.data.remote

import okhttp3.OkHttpClient
import pl.wmwdev.beaconservice.data.Action
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

object RetrofitRepo {
    private const val BASE_URL = "https://actioncreatorapi.onrender.com/"

    val api : ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
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