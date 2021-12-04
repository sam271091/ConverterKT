package demo.com.converterkt.api


import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object ApiFactory {
    private const val BASE_URL = "https://www.cbar.az/currencies/"

    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val  apiService = retrofit.create(ApiService:: class.java)
}