package Apps.com.converterkt.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object GoogleMapsApiFactory {
    private const val BASE_URL = "https://maps.googleapis.com"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(GoogleMapsApiFactory.BASE_URL)
        .build()

    val  apiService = GoogleMapsApiFactory.retrofit.create(GoogleMapsApiService:: class.java)

}

