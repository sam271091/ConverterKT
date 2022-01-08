package Apps.com.converterkt

import android.app.Application

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import Apps.com.converterkt.api.ApiFactory
import Apps.com.converterkt.database.AppDatabase
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.pojo.ValuteInfo
import Apps.com.converterkt.utils.getAZN
import Apps.com.converterkt.utils.getCurrentTime
import Apps.com.converterkt.utils.getValutePresentation
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class ConverterViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private var compositeDisposable = CompositeDisposable()
    val valutesList = db.converterDao().getAllValutes()
    val allValuteInfo = db.converterDao().getAllValuteInfo()
    val currDate = getCurrentTime()
    var valutes : (filter:String) -> LiveData<List<Valute>> = {db.converterDao().getFilteredValutes(it)}


    val valutesNames = getValutePresentation()


    init {
        loadData()
    }

    fun getFilteredList(filter:ArrayList<Valute>): LiveData<List<ValuteInfo>> {

        return db.converterDao().getAllValuteInfoFiltered(filter)
    }

     fun loadData(){

         var datePr = SimpleDateFormat("dd.MM.yyyy").format(currDate)
         val disposable = ApiFactory.apiService.getData(datePr + ".xml")
            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
            .delaySubscription(1,TimeUnit.SECONDS)
//            .repeat()
            .retry()
            .subscribe({
//                Log.d("TEST_OF_LOADING_DATA",it.toString())
                try {
                    var valutes = it.valType?.get(1)?.valute

                    valutes?.map { it.name = valutesNames.get(it.code).toString() }

                    db.converterDao().insertValutes(valutes)

                    db.converterDao().insertValute(getAZN())

                    valutes.let {

                        var valuteInfo = ArrayList<ValuteInfo>()
                        if (it != null) {
                            valuteInfo.add(ValuteInfo(date = currDate,valute = getAZN(), nominal = 1.0, value = 1.0))

                            for (valute in it){

                                valuteInfo.add(ValuteInfo(date = currDate, valute = valute, nominal = valute.nominal.toDouble(), value = valute.value))
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