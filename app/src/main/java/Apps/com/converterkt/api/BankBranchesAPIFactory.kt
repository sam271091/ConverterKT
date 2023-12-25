package Apps.com.converterkt.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object BankBranchesAPIFactory {
    private const val BASE_URL = "http://64.23.129.171/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BankBranchesAPIFactory.BASE_URL)
        .build()

    val  apiService = BankBranchesAPIFactory.retrofit.create(BankBranchesAPIService:: class.java)
}