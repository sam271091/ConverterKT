package Apps.com.converterkt

import android.app.Application

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import Apps.com.converterkt.api.ApiFactory
import Apps.com.converterkt.api.BanksAPIFactory
import Apps.com.converterkt.api.GoogleMapsApiFactory
import Apps.com.converterkt.database.AppDatabase
import Apps.com.converterkt.pojo.*
import Apps.com.converterkt.utils.getAZN
import Apps.com.converterkt.utils.getCurrentTime
import Apps.com.converterkt.utils.getValutePresentation
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ConverterViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private var compositeDisposable = CompositeDisposable()
    val valutesList = db.converterDao().getAllValutes()
//    val allValuteInfo = db.converterDao().getAllValuteInfo()
//    val allValuteInfo : (chosenDate:Date)-> LiveData<List<ValuteInfo>> = {db.converterDao().getAllValuteInfo(it)}
    val allValuteInfo : (chosenDate:Date)-> Flow<List<ValuteInfo>> = {db.converterDao().getAllValuteInfo(it)}
    var currDate = getCurrentTime()
//    var valutes : (filter:String) -> LiveData<List<Valute>> = {db.converterDao().getFilteredValutes(it)}
    var valutes : (filter:String) -> Flow<List<Valute>> = {db.converterDao().getFilteredValutes(it)}
    val valutesNames = getValutePresentation()

    val allBanksInfo = db.converterDao().getAllBankInfos()
    val allBanksData = db.converterDao().getAllBanksData()
//    val banksDataByValute :(filter:String) -> LiveData<List<BankInfo>> =  {db.converterDao().getBanksDataByValute(it)}
    val banksDataByValute :(filter:String) -> Flow<List<BankInfo>> =  {db.converterDao().getBanksDataByValute(it)}

    val favoriteValutes = db.converterDao().getFavouriteValutesForFilter()

    init {
//        loadDataByDate(currDate)
//        loadBanksInfo()
    }


    fun loadData(){
        loadDataByDate(currDate)
        loadBanksInfo()
    }



    fun loadBanksInfo(){
        val disposable = BanksAPIFactory.apiService.getBanksRates()
            .subscribeOn(Schedulers.io())
            .delaySubscription(1,TimeUnit.SECONDS)
            .retry()
            .subscribe({


                db.converterDao().clearBankInfo()
                db.converterDao().insertBankInfos(it)

            },{
                Log.d("ERROR_LOADING_BI",it.localizedMessage)
            })

    }


    fun loadBankBranches(bankInfo:BankInfo){
        val disposable = GoogleMapsApiFactory.apiService.getBankLocation(bankCode = bankInfo.bank)
            .subscribeOn(Schedulers.io())
            .delaySubscription(1,TimeUnit.SECONDS)
            .retry()
            .subscribe({


                var bankLocationModel = it
                var results = it.results

                if (results.size != 0){
                    db.converterDao().clearBranch(bankInfo.bank)
                }

                results.onEach {

                var bankBranch = BankBranch(bankCode = bankInfo.bank, branchName = it.name, vicinity = it.vicinity,
                    lat = it.geometry.location.lat.toString(), lng = it.geometry.location.lng.toString())
                db.converterDao().insertbankBranch(bankBranch)
                }


            },{
                Log.d("ERROR_LOADING_BI",it.localizedMessage)
            })
    }


    fun loadDataByDate(chosenDate:Date){
        currDate = chosenDate
        var calendar = Calendar.getInstance()
        calendar.time = chosenDate
        calendar.add(Calendar.DAY_OF_YEAR, -21)

        var minDate = calendar.time

        while (currDate >= (minDate)){

            loadData(currDate)
            var calendar = Calendar.getInstance()
            calendar.time = currDate
            calendar.add(Calendar.DAY_OF_YEAR, -7)

            currDate = calendar.time
        }
    }


//    fun getAllBankInfo() : LiveData<List<BankInfo>>{
//        return db.converterDao().getAllBankInfos()
//    }

//    fun getFilteredList(filter:ArrayList<Valute>,chosenDate:Date): LiveData<List<ValuteInfo>> {
//
//        return db.converterDao().getAllValuteInfoFiltered(filter,chosenDate)
//    }

    fun getFilteredList(filter:ArrayList<Valute>,chosenDate:Date): Flow<List<ValuteInfo>> {

        return db.converterDao().getAllValuteInfoFiltered(filter,chosenDate)
    }


    fun getFilteredListNonObservable(filter:ArrayList<Valute>,chosenDate:Date): List<ValuteInfo> {

        return db.converterDao().getAllValuteInfoFilteredNonObservable(filter,chosenDate)
    }

    fun getDataForTheGraph(valute: Valute): List<ValuteInfo> {
        return db.converterDao().getDataForTheGraph(valute)
    }

//    fun getDataCurrencyItem(valute: Valute): LiveData<List<ValuteInfo>> {
//        return db.converterDao().getDataCurrencyItem(valute)
//    }

    fun getDataCurrencyItem(valute: Valute): Flow<List<ValuteInfo>> {
        return db.converterDao().getDataCurrencyItem(valute)
    }

    fun getFilteredValuteInfo(valute:Valute,chosenDate:Date):ValuteInfo {

        return db.converterDao().getFilteredValuteInfo(valute,chosenDate)
    }

    fun getValuteByCode(code:String):Valute{
        return db.converterDao().getValuteByCode(code)
    }



    fun getFavoriteValuteById(valute: Valute) : FavoriteValute?{
        return db.converterDao().getFavouriteValuteById(valute.code)
    }


    fun insertFavoriteValute(valute: Valute){
        var favoriteValute = FavoriteValute(valute)
        db.converterDao().insertFavoriteValute(favoriteValute)
    }


    fun deleteFavoriteValute(valute: Valute){
        var favoriteValute = FavoriteValute(valute)
        db.converterDao().deleteFavouriteValute(favoriteValute)
    }


    fun getFavoriteValutes() : List<FavoriteValute?>{
        return db.converterDao().getFavouriteValutes()
    }





     fun loadData(downloadDate:Date){

         var datePr = SimpleDateFormat("dd.MM.yyyy").format(downloadDate)
         val disposable = ApiFactory.apiService.getData(datePr + ".xml")
            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
            .delaySubscription(1,TimeUnit.SECONDS)
//            .repeat()
            .retry()
            .subscribe({
//                Log.d("TEST_OF_LOADING_DATA",it.toString())
                try {
                    var valutes = it.valType?.get(1)?.valute?.filter { !it.code.equals("SDR") }

                    valutes?.map { it.name = valutesNames.get(it.code).toString() }

                    db.converterDao().insertValutes(valutes)

                    db.converterDao().insertValute(getAZN())

                    valutes.let {

                        var valuteInfo = ArrayList<ValuteInfo>()
                        if (it != null) {
                            valuteInfo.add(ValuteInfo(date = downloadDate,valute = getAZN(), nominal = 1.0, value = 1.0))

                            for (valute in it){

                                valuteInfo.add(ValuteInfo(date = downloadDate, valute = valute, nominal = valute.nominal.toDouble(), value = valute.value))
                            }

                            db.converterDao().insertValuteInfos(valuteInfo)

                        }
                    }





                } catch (e:Throwable){
                    Log.d("TEST_OF_LOADING_DATA",e.toString())
                }

                       },
                {
                Log.d("TEST_OF_LOADING_DATA",it.localizedMessage)
            })
        compositeDisposable.add(disposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}