package demo.com.converterkt

import android.app.Activity
import android.content.ClipData.newIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import demo.com.converterkt.api.ApiFactory
import demo.com.converterkt.pojo.Valute
import demo.com.converterkt.pojo.ValuteInfo
import demo.com.converterkt.utils.getAZN
import demo.com.converterkt.utils.getCurrentTime
import demo.com.converterkt.utils.getValuteFlagPath
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ConverterViewModel

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    var firstValute : ValuteInfo? = null
    var secondValute : ValuteInfo? = ValuteInfo(getCurrentTime(), getAZN())
    lateinit var chosenField : TextView
    lateinit var chosenImage : ImageView

    private var precision =  DecimalFormat("#,##0.0000")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chosenField = tvValute1
        chosenImage = imageValute1

        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]
        viewModel.valutesList.observe(this, Observer {
            Log.d("TEST_OF_LOADING_DATA","Success in activity : $it")
        })

        viewModel.allValuteInfo.observe(this, Observer {
            Log.d("TEST_OF_LOADING_DATA","Success in activity : $it")
        })


        tvValute1.setOnClickListener(View.OnClickListener {
            chosenField = tvValute1
            chosenImage = imageValute1
            openValuteInfoListChooser()
        })


        tvValute2.setOnClickListener(View.OnClickListener {
            chosenField = tvValute2
            chosenImage = imageValute2
            openValuteInfoListChooser()
        })





         resultLauncher = registerForActivityResult(MySecondActivityContract()) { result ->

             if (result != null){
                 var valuteInfo = result

                 if (chosenField ==tvValute1){
                     firstValute = valuteInfo
                     setValutePresentation(firstValute)
                 } else {
                     secondValute = valuteInfo
                     setValutePresentation(secondValute)
                 }

                 calculateResult()
             }





        }

        editTextSum.addTextChangedListener {
            calculateResult()
        }

    }


    fun calculateResult(){
        var sumValue = editTextSum.text.toString()

        var Res = 0.0

       if (!sumValue.equals("")){
           var sumDouble : Double? = sumValue.toDouble()

           var valuteInfoForCalc: ValuteInfo? = null

           var crossCal = false
           if (firstValute?.valute?.code.equals(getAZN().code) ){
               valuteInfoForCalc = secondValute
           } else if (secondValute?.valute?.code.equals(getAZN().code)){
                valuteInfoForCalc = firstValute
           } else if (!firstValute?.valute?.code.equals(getAZN().code) &&
                   !secondValute?.valute?.code.equals(getAZN().code)){
               valuteInfoForCalc = firstValute
               crossCal = true
           }


           valuteInfoForCalc.let {
               var value = it?.value
               var nominal = it?.nominal
               if (value != null && nominal != null && sumDouble != null){
                   if (crossCal){

                       var secvalue = secondValute?.value
                       var secnominal = secondValute?.nominal

                       if (secvalue != null && secnominal != null){
                           Res =  (sumDouble * value/nominal)  / secvalue*secnominal
                       } else {
                           Res = 0.0
                       }

                   }
                   else if (valuteInfoForCalc == firstValute ){
                       Res =  sumDouble * value/nominal
                   } else if (valuteInfoForCalc == secondValute) {
                       Res =  sumDouble / value*nominal
                   }

               }

           }
       }


        tvResult.text = precision.format(Res)

    }


      fun setValutePresentation(valuteInfo:ValuteInfo?){



          Picasso.get().load(getValuteFlagPath(valuteInfo?.valute)).into(chosenImage)

          chosenField.text = "${valuteInfo?.valute?.code}  ${valuteInfo?.valute?.name}"


//          chosenField.setCompoundDrawables(img.drawable,null,null,null)
      }


    fun onClickSwitcher(view: android.view.View) {
//        openValuteInfoListChooser()

        var intermediateValute = firstValute
        firstValute = secondValute
        chosenImage = imageValute1
        chosenField = tvValute1
        setValutePresentation(firstValute)
        secondValute = intermediateValute
        chosenImage = imageValute2
        chosenField = tvValute2
        setValutePresentation(secondValute)
        calculateResult()


    }

    fun openValuteInfoListChooser(){
        val intent = ValuteListActivity.newIntent(this)
        resultLauncher.launch(intent)


    }


}


