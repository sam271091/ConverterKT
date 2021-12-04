package demo.com.converterkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import demo.com.converterkt.api.ApiFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val disposable = ApiFactory.apiService.getData("03.12.2021.xml")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("TEST_OF_LOADING_DATA",it.toString())},{
                Log.d("TEST_OF_LOADING_DATA",it.localizedMessage)
            })
        compositeDisposable.add(disposable)

    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}


