package Apps.com.converterkt.api

import Apps.com.converterkt.pojo.BankInfo
import io.reactivex.Single
import retrofit2.http.GET

interface BanksApiService {
    @GET("getBanksRates")
    fun getBanksRates() : Single<ArrayList<BankInfo>>
}