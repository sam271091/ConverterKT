package Apps.com.converterkt.api

import Apps.com.converterkt.pojo.ValCurs
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    fun getData(@Url url:String) : Single<ValCurs>
}