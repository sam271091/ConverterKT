package Apps.com.converterkt.api

import Apps.com.converterkt.pojo.BankLocationModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApiService {

    @GET("maps/api/place/nearbysearch/json")
    fun getBankLocation(@Query("name") bankCode: String?,
                        @Query("location") location: String = LOCATION,
                        @Query("radius") radius: Int = RADIUS,
                        @Query("types") types: String = TYPES,
                        @Query("key") key: String = KEY): Single<BankLocationModel>


    companion object{
        const val KEY = "AIzaSyAHNe6jOSqzUOMCIOKWPOdh1HAUKmTo_SI"
        const val LOCATION = "40.409264, 49.867092"
        const val RADIUS = 50000
        const val TYPES = "bank,postoffice"
    }


}