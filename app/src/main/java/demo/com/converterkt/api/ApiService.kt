package demo.com.converterkt.api

import demo.com.converterkt.pojo.ValCurs
import demo.com.converterkt.pojo.Valute
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    fun getData(@Url url:String) : Single<ValCurs>
}