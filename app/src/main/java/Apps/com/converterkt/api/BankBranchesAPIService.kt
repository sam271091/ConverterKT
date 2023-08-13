package Apps.com.converterkt.api

import Apps.com.converterkt.pojo.BankBranch
import io.reactivex.Single
import retrofit2.http.GET

interface BankBranchesAPIService {

    @GET("getBanksBranches")
    fun getBankLocation(): Single<ArrayList<BankBranch>>




//    companion object{
//
//    }
}