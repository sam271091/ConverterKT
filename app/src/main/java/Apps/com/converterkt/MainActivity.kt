package Apps.com.converterkt


import Apps.com.converterkt.pojo.Valute
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
import Apps.com.converterkt.utils.dpToPx
import Apps.com.converterkt.utils.getAZN
import Apps.com.converterkt.utils.getCurrentTime
import Apps.com.converterkt.utils.getValuteFlagPath
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.Observer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import com.jjoe64.graphview.DefaultLabelFormatter
import java.text.SimpleDateFormat
import android.graphics.Paint
import android.preference.PreferenceManager
import android.widget.DatePicker
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope



import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.util.*
import kotlin.collections.ArrayList
import com.jjoe64.graphview.series.DataPointInterface

import com.jjoe64.graphview.series.PointsGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries.CustomShape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ConverterViewModel

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    var firstValute : ValuteInfo? = null
    var secondValute : ValuteInfo? = ValuteInfo(getCurrentTime(), getAZN())
    lateinit var chosenField : TextView
    lateinit var chosenImage : ImageView

    private var precision =  DecimalFormat("#,##0.0000")
//    private var precisionInput =  DecimalFormat("#,##0.0000")

    var fraction = false

    var additionalValue = false

    var sdf = SimpleDateFormat(" dd.MM.yy")

    var dotIsPressed = false

    var calendar = Calendar.getInstance()

    lateinit var chosenDate:Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)


        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        chosenField = tvValute1
        chosenImage = imageValute1


        chosenDate = calendar.time

        setDatePresentation()

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
                     makeGraph(firstValute)

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

        graphView.getGridLabelRenderer().isHorizontalLabelsVisible = false
        graphView.getGridLabelRenderer().isVerticalLabelsVisible   = false


        imageValute1.setOnClickListener {
            openCurrencyItem(firstValute?.valute)
        }

        imageValute2.setOnClickListener {
            openCurrencyItem(secondValute?.valute)
        }


        readSavedValutes()

        setDatePresentation()


        textViewDate.setOnClickListener {

            setDate()
        }


        imageDownloadbtn.setOnClickListener {
            viewModel.loadDataByDate(chosenDate)
        }

    }



    fun setDate(){

        var datePicker = DatePickerDialog(this,
            { view, year, month, day ->

                calendar.set(year,month,day)

                chosenDate = calendar.time

                setDatePresentation()

                setValuteInfoByDate()

            },calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)
            )
        datePicker.show()
    }


    fun setDatePresentation(){
        textViewDate.text = SimpleDateFormat("dd.MM.yyyy").format(chosenDate)
    }

    fun readSavedValutes(){

        var preferences = getPreferences(Context.MODE_PRIVATE)


        var firstSavedValute = preferences.getString("firstValute","")

        var secondSavedValute = preferences.getString("secondValute","")

        if (firstSavedValute != "" || secondSavedValute != ""){


            GlobalScope.launch{

                firstValute = viewModel.getFilteredValuteInfo(viewModel.getValuteByCode(firstSavedValute as String),chosenDate)

                secondValute = viewModel.getFilteredValuteInfo(viewModel.getValuteByCode(secondSavedValute as String),chosenDate)

                withContext(Dispatchers.Main) {

                    chosenImage = imageValute1
                    setValutePresentation(firstValute,tvValute1)
                makeGraph(firstValute)

                    chosenImage = imageValute2
                setValutePresentation(secondValute,tvValute2)
                }


            }


        }
    }

    fun setValuteInfoByDate(){
        GlobalScope.launch{
            firstValute = viewModel.getFilteredValuteInfo(firstValute?.valute as Valute,chosenDate)

            secondValute = viewModel.getFilteredValuteInfo(secondValute?.valute as Valute,chosenDate)

            calculateResult()
        }
    }

    override fun onPause() {
        adView.pause()
        super.onPause()

    }

    override fun onResume() {
        adView.resume()
        super.onResume()
    }

    override fun onDestroy() {
        saveValuteInfo()

        adView.destroy()
        super.onDestroy()
    }


    fun saveValuteInfo(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {

            if (firstValute != null){
                putString("firstValute", firstValute?.valute?.code)
            }

            if (secondValute != null){
                putString("secondValute", secondValute?.valute?.code)
            }


            apply()
        }
    }



    fun makeGraph(currValuteInfo:ValuteInfo?){

        graphView.removeAllSeries()

        currValuteInfo?.valute?.let {
//            viewModel.getDataForTheGraph(it).observe(this, Observer {
//
//
//                createGraph(it as ArrayList<ValuteInfo>)
//
//
//            })


            lifecycleScope.launch(Dispatchers.IO){
                var graphData = viewModel.getDataForTheGraph(it)

                createGraph(graphData as ArrayList<ValuteInfo>)
            }


        }





    }

    fun createGraph(graphData:ArrayList<ValuteInfo>){

        graphView.getGridLabelRenderer().isHorizontalLabelsVisible = true
        graphView.getGridLabelRenderer().isVerticalLabelsVisible   = true

        graphData.sortBy { it.date }


        if (graphData.size > 0){
            if (firstValute?.valute == graphData[0].valute){

                graphView.removeAllSeries()

                var dataPoints = mutableListOf<DataPoint>()

                for (valuteInfo in graphData){


                    dataPoints.add(DataPoint(valuteInfo.date, valuteInfo.value))
                }

                var firstDate = Date()
                var lastDate =  Date()

                if (graphData.size > 0){
                    firstDate = graphData[0].date
                    lastDate = graphData[graphData.size-1].date
                }



                val series = LineGraphSeries(dataPoints.toTypedArray())


//                val seriesPoints = PointsGraphSeries<DataPoint>(dataPoints.toTypedArray())
//                seriesPoints.setCustomShape { canvas, paint, x, y, dataPoint ->
//                    paint.color = Color.BLACK
//                    paint.textSize = 20f
//                    canvas.drawText(dataPoint.y.toString(), x, y, paint)
//                }



                series.setAnimated(true)
                series.setDrawDataPoints(true)
                series.setDataPointsRadius(15F)
                series.setThickness(8)



                series.setDrawBackground(true)
                series.setDrawDataPoints(true)




                graphView.addSeries(series)


                graphView.getGridLabelRenderer().setLabelFormatter(object : DefaultLabelFormatter() {
                    override fun formatLabel(value: Double, isValueX: Boolean): String {
                        return if (isValueX) {
                            sdf.format(value)
                        } else {
//                            super.formatLabel(value, isValueX)
                            precision.format(value)
                        }
                    }
                })


                graphView.getGridLabelRenderer().setNumHorizontalLabels(5)


                graphView.getViewport().setMinX(firstDate.getTime().toDouble())
                graphView.getViewport().setMaxX(lastDate.getTime().toDouble())
                graphView.getViewport().setXAxisBoundsManual(false)

                graphView.getViewport().setScalable(true)
//
                graphView.getGridLabelRenderer().setHorizontalLabelsAngle(30)
                graphView.getGridLabelRenderer().setHumanRounding(true)

//                graphView.getGridLabelRenderer().setVerticalLabelsColor(R.color.white);
//                graphView.getGridLabelRenderer().setHorizontalLabelsColor(R.color.white)



                graphView.getGridLabelRenderer().reloadStyles()
            }
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


      fun setValutePresentation(valuteInfo:ValuteInfo?,fieldToChange:TextView = chosenField){

          chosenImage.layoutParams.width = 0

//          Picasso.get().load(getValuteFlagPath(valuteInfo?.valute)).into(chosenImage)

          if (valuteInfo != null){
              fieldToChange.text = "${valuteInfo?.valute?.code}"
              Picasso.get().load(getValuteFlagPath(valuteInfo?.valute)).into(chosenImage)
              chosenImage.layoutParams.width = dpToPx(58,resources.displayMetrics.density)
              chosenImage.layoutParams.height= dpToPx(58,resources.displayMetrics.density)
          }



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

        makeGraph(firstValute)

    }

    fun openValuteInfoListChooser(){
        val intent = ValuteListActivity.newIntent(this)
        intent.putExtra("chosenDate",chosenDate.time)
        resultLauncher.launch(intent)


    }


    fun openCurrencyItem(valute:Valute?){
        val intent = ActivityCurrencyItem.newIntent(this)
        intent.putExtra("currency",valute)
        startActivity(intent)
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
        makeGraph(firstValute)

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

        if (number==46){
            dotIsPressed = true
        }

        if (!sumValue.contains(".") && sumValue.take(1).contains("0") && number!=46
            && number!=0){
            editTextSum.setText("0.00")
            return
        }


        if (sumValue == "0.00" && number!=46 && !dotIsPressed){
            editTextSum.setText(number.toString())
        }
        else if (number==46){
            if (!sumValue.contains(".")){
                editTextSum.setText(sumValue + ".")
            }

        } else {



            editTextSum.setText(sumValue + number)
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

        dotIsPressed = false
        fraction = false
        additionalValue = false
        editTextSum.setText("0.00")
    }
}


