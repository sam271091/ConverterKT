package demo.com.converterkt

import android.app.Application

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import demo.com.converterkt.api.ApiFactory
import demo.com.converterkt.database.AppDatabase
import demo.com.converterkt.pojo.ValuteInfo
import demo.com.converterkt.utils.getCurrentTime
import io.reactivex.android.schedulers.AndroidSchedulers
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



    init {
        loadData()
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
                Log.d("TEST_OF_LOADING_DATA",it.toString())
                try {
                    var valutes = it.valType?.get(1)?.valute
                    db.converterDao().insertValutes(valutes)

                    valutes.let {

                        var valuteInfo = ArrayList<ValuteInfo>()
                        if (it != null) {
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