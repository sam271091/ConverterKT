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
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ConverterViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private var compositeDisposable = CompositeDisposable()
    val valutesList = db.converterDao().getAllValutes()
    val allValuteInfo = db.converterDao().getAllValuteInfo()
    var currDate = getCurrentTime()
    var valutes : (filter:String) -> LiveData<List<Valute>> = {db.converterDao().getFilteredValutes(it)}


    val valutesNames = getValutePresentation()


    init {
        var calendar = Calendar.getInstance()
        calendar.time = currDate
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

    fun getFilteredList(filter:ArrayList<Valute>): LiveData<List<ValuteInfo>> {

        return db.converterDao().getAllValuteInfoFiltered(filter)
    }

    fun getDataForTheGraph(valute: Valute): LiveData<List<ValuteInfo>> {
        return db.converterDao().getDataForTheGraph(valute)
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