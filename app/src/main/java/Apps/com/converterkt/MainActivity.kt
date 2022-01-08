package Apps.com.converterkt


import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import Apps.com.converterkt.pojo.ValuteInfo
import Apps.com.converterkt.utils.getAZN
import Apps.com.converterkt.utils.getCurrentTime
import Apps.com.converterkt.utils.getValuteFlagPath
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
//        viewModel.valutesList.observe(this, Observer {
//            Log.d("TEST_OF_LOADING_DATA","Success in activity : $it")
//        })
//
//        viewModel.allValuteInfo.observe(this, Observer {
//            Log.d("TEST_OF_LOADING_DATA","Success in activity : $it")
//        })


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
                 val valuteInfo = result

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
        val sumValue = editTextSum.text.toString()

        var Res = 0.0

       if (!sumValue.equals("")){
           val sumDouble : Double = sumValue.toDouble()

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
               val value = it?.value
               val nominal = it?.nominal
               if (value != null && nominal != null ){
                   if (crossCal){

                       val secvalue = secondValute?.value
                       val secnominal = secondValute?.nominal

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

        val intermediateValute = firstValute
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

//    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
//
//        outState.putSerializable("firstValute",firstValute)
//        outState.putSerializable("secondValute",secondValute)
//        outState.putString("editTextSum",editTextSum.text.toString())
//
//    }



//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//
////        editTextSum.text  = savedInstanceState.getString("editTextSum")
//        firstValute = savedInstanceState.getSerializable("firstValute") as ValuteInfo?
//        chosenImage = imageValute1
//        chosenField = tvValute1
//        setValutePresentation(firstValute)
//        secondValute = savedInstanceState.getSerializable("secondValute") as ValuteInfo?
//        chosenImage = imageValute2
//        chosenField = tvValute2
//        setValutePresentation(secondValute)
//        calculateResult()
//    }


    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        savedInstanceState.putSerializable("firstValute",firstValute)
        savedInstanceState.putSerializable("secondValute",secondValute)
        savedInstanceState.putString("editTextSum",editTextSum.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)


        firstValute = savedInstanceState.getSerializable("firstValute") as ValuteInfo?
        chosenImage = imageValute1
        chosenField = tvValute1
        setValutePresentation(firstValute)
        secondValute = savedInstanceState.getSerializable("secondValute") as ValuteInfo?
        chosenImage = imageValute2
        chosenField = tvValute2
        setValutePresentation(secondValute)
        calculateResult()

    }
}


