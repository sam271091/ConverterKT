package demo.com.converterkt

import android.app.Activity
import android.content.ClipData.newIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import demo.com.converterkt.api.ApiFactory
import demo.com.converterkt.pojo.Valute
import demo.com.converterkt.pojo.ValuteInfo
import demo.com.converterkt.utils.getCurrentTime
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ConverterViewModel

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    var firstValute : ValuteInfo? = null
    var secondValute : ValuteInfo? = ValuteInfo(getCurrentTime(),Valute(code = "AZN", name = "Azerbaijan manat", nominal = "1", value = 1.0))
    lateinit var chosenField : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chosenField = tvValute1

        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]
        viewModel.valutesList.observe(this, Observer {
            Log.d("TEST_OF_LOADING_DATA","Success in activity : $it")
        })

        viewModel.allValuteInfo.observe(this, Observer {
            Log.d("TEST_OF_LOADING_DATA","Success in activity : $it")
        })


        tvValute1.setOnClickListener(View.OnClickListener {
            chosenField = tvValute1
            openValuteInfoListChooser()
        })


        tvValute2.setOnClickListener(View.OnClickListener {
            chosenField = tvValute2
            openValuteInfoListChooser()
        })





         resultLauncher = registerForActivityResult(MySecondActivityContract()) { result ->

             var valuteInfo = result

             if (chosenField ==tvValute1){
                 firstValute = valuteInfo
                 setValutePresentation(firstValute)
             } else {
                 secondValute = valuteInfo
                 setValutePresentation(secondValute)
             }




        }

    }


      fun setValutePresentation(valuteInfo:ValuteInfo?){
          chosenField.text = "${valuteInfo?.valute?.code}  ${valuteInfo?.valute?.name}"
      }


    fun onClick(view: android.view.View) {
        openValuteInfoListChooser()
    }

    fun openValuteInfoListChooser(){
        val intent = ValuteListActivity.newIntent(this)
        resultLauncher.launch(intent)


    }


}


