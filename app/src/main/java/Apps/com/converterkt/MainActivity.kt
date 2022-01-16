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
import kotlin.math.truncate

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ConverterViewModel

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    var firstValute : ValuteInfo? = null
    var secondValute : ValuteInfo? = ValuteInfo(getCurrentTime(), getAZN())
    lateinit var chosenField : TextView
    lateinit var chosenImage : ImageView

    private var precision =  DecimalFormat("#,##0.00")

    var fraction = false

    var additionalValue = false

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

          chosenField.text = "${valuteInfo?.valute?.code}"


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




    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        savedInstanceState.putSerializable("firstValute",firstValute)
        savedInstanceState.putSerializable("secondValute",secondValute)
        savedInstanceState.putString("editTextSum",editTextSum.text.toString())
        savedInstanceState.putBoolean("fraction",fraction)
        savedInstanceState.putBoolean("additionalValue",additionalValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        fraction = savedInstanceState.getBoolean("fraction")
        additionalValue = savedInstanceState.getBoolean("additionalValue")

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



    fun onClickAction(view: View){

        var btnId = view.resources.getResourceEntryName(view.id)

        when(btnId){
            "btn_1" -> performAction(1.toChar())
            "btn_2" -> performAction(2.toChar())
            "btn_3" -> performAction(3.toChar())
            "btn_4" -> performAction(4.toChar())
            "btn_5" -> performAction(5.toChar())
            "btn_6" -> performAction(6.toChar())
            "btn_7" -> performAction(7.toChar())
            "btn_8" -> performAction(8.toChar())
            "btn_9" -> performAction(9.toChar())
            "btn_0" -> performAction(0.toChar())
            "btn_dot" -> performAction(".".single())
            else -> deleteSymbol()

        }
    }

    fun performAction(actionSymbol:Char){

        var number = actionSymbol.code

        val sumValue = editTextSum.text.toString()

        var sumDouble  = sumValue.toDouble()

        var newSumDouble = 0.0

        var newSumString = "0"

        if (number == 46){

            fraction = true

        } else {



            if (!fraction){
                newSumDouble = sumDouble*10+number

                newSumString = newSumDouble.toString()

            } else {
//                if (number!=0){
//                    newSumDouble = sumDouble+number.toDouble()/10
//                } else {
//                    newSumDouble = sumDouble + 1/10
//                }

                var iPart = truncate(sumDouble)
                var fPart = precision.format(sumDouble.minus(iPart)).replace(",",".").toDouble()

                var fPartLength = fPart.toString().length-2

                var fractionCapacity = 10.0

                if (fPart==0.0 && !additionalValue){
                     fractionCapacity =  Math.pow(10.0,fPartLength.toDouble())
                } else {
                    fractionCapacity =  Math.pow(10.0,fPartLength.toDouble()+1)
                }



                fPart =(fPart*fractionCapacity+number)/fractionCapacity





                newSumDouble = iPart + fPart

                newSumString = newSumDouble.toString()

                if (number ==0){

                    newSumString = (newSumDouble.toString() + "0")

                    additionalValue = true
                }

            }

            editTextSum.setText(newSumString)

        }



    }

    fun deleteSymbol(){

//        var sumValue = editTextSum.text.toString()
//        var newSumValue = sumValue.dropLast(1)
//
////        var sumDouble  = newSumValue.toDouble()
//
//        if (newSumValue.equals("")){
//            newSumValue = "0.0000"
//        }

        fraction = false
        additionalValue = false
        editTextSum.setText("0.00")
    }
}


