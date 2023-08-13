package Apps.com.converterkt


import Apps.com.converterkt.fragments.banksResultsFragment
import Apps.com.converterkt.fragments.favorites_Converter_Fragment
import Apps.com.converterkt.fragments.graph_Fragment
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
import Apps.com.converterkt.utils.*
import android.app.DatePickerDialog
import android.content.Context
import android.text.InputType
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import androidx.fragment.app.Fragment


import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import java.util.*
import kotlin.collections.ArrayList

import kotlinx.coroutines.*



class MainActivity : AppCompatActivity() {

//    private lateinit var viewModel: ConverterViewModel

    private  val viewModel: ConverterViewModel by viewModels()


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

    var currTabPosition : Int? =  0

//    var symbolRemoved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this)


        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        chosenField = tvValute1
        chosenImage = imageValute1


        chosenDate = getEndOfTheDay(calendar.time)


        viewModel.loadData()

//        collectLatestLifecycleFlow(viewModel.allBanksData){
//            it.onEach {
//                viewModel.loadBankBranchesGooglePlaces(it)
//            }
//        }

        viewModel.loadBankBranches()


        setDatePresentation()

//        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]

//        viewModel: ConverterViewModel by activityViewModels {
//            ConverterViewModelFactory(
//                (application as ConverterApplication).database.converterDao()
//            )
//        }

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



//        editTextSum.setRawInputType(InputType.TYPE_CLASS_TEXT);
//        editTextSum.setTextIsSelectable(true);
//        editTextSum.setFocusable(true);
        editTextSum.setShowSoftInputOnFocus(false);

        editTextSum.addTextChangedListener {
            calculateResult()
        }

//        editTextSum.setOnFocusChangeListener { view, b ->
//
//            if (b){
//                Toast.makeText(this, "focused", Toast.LENGTH_SHORT).show()
//
//            } else {
//                Toast.makeText(this, "not focused", Toast.LENGTH_SHORT).show()
//            }
//        }



//        editTextSum.setOnTouchListener { view, motionEvent ->
//
//            Toast.makeText(this, "touched", Toast.LENGTH_SHORT).show()
//
//            false
//        }

//        graphView.getGridLabelRenderer().isHorizontalLabelsVisible = false
//        graphView.getGridLabelRenderer().isVerticalLabelsVisible   = false


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


        banksBtn.setOnClickListener {

            val intent = BanksDataActivity.newIntent(this)
            startActivity(intent)
        }


//        viewModel.allValuteInfo(chosenDate).observe(this, Observer {
//            setValuteInfoByDate()
//        })

        collectLatestLifecycleFlow(viewModel.allValuteInfo(chosenDate)){
            setValuteInfoByDate()
        }


        tabsChangerMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                var currentPos = tab?.position

                currTabPosition = currentPos

                when(currentPos){
                    0->replaceFragment(graph_Fragment(firstValute,viewModel))
                    1->replaceFragment(favorites_Converter_Fragment(firstValute,viewModel,editTextSum.text.toString(),chosenDate))
                    2->replaceFragment(banksResultsFragment(firstValute,viewModel,editTextSum.text.toString()))
                }

            }



            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        replaceFragment(graph_Fragment(firstValute,viewModel))



    }


    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView_Main,fragment)
        fragmentTransaction.commit()
    }


    fun setDate(){

        var datePicker = DatePickerDialog(this,
            { view, year, month, day ->

                calendar.set(year,month,day)

                chosenDate = getEndOfTheDay(calendar.time)


                setDatePresentation()
                viewModel.loadDataByDate(getStartOfTheDay(chosenDate))



                if (!checkForInternet(this)){
                    setValuteInfoByDate()
                }


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

        if (!firstSavedValute.equals("")  && !secondSavedValute.equals("")){


            CoroutineScope(Dispatchers.IO).launch{

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
        runBlocking (Dispatchers.IO) {
            CoroutineScope(Dispatchers.IO).launch{

                try {



                    var firstValuteData = viewModel.getFilteredValuteInfo(firstValute?.valute as Valute,chosenDate)
                    var secondValuteData = viewModel.getFilteredValuteInfo(secondValute?.valute as Valute,chosenDate)

                    if (firstValuteData!=null && secondValuteData != null){
                        firstValute = firstValuteData

                        secondValute = secondValuteData

                        calculateResult()
                    } else {
                        firstValute = null
                        secondValute = null


//                        chosenImage = imageValute1
//                        setValutePresentation(firstValute,tvValute1)
//                        chosenImage = imageValute2
//                        setValutePresentation(secondValute,tvValute2)
                        throw Exception("null data")
                    }




                } catch (e:Throwable){
//                    Toast.makeText(applicationContext, "No data available for this date. Unable to convert.", Toast.LENGTH_SHORT).show()
                    tvResult.text = precision.format(0.0)
                }

            }
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

        if (currTabPosition==0){
            replaceFragment(graph_Fragment(firstValute,viewModel))
        }



//        graphView.removeAllSeries()
//
//        currValuteInfo?.valute?.let {
////            viewModel.getDataForTheGraph(it).observe(this, Observer {
////
////
////                createGraph(it as ArrayList<ValuteInfo>)
////
////
////            })
//
//
//            lifecycleScope.launch(Dispatchers.IO){
//                var graphData = viewModel.getDataForTheGraph(it)
//
//                createGraph(graphData as ArrayList<ValuteInfo>)
//            }
//
//
//        }





    }

    fun createGraph(graphData:ArrayList<ValuteInfo>){

//        graphView.getGridLabelRenderer().isHorizontalLabelsVisible = true
//        graphView.getGridLabelRenderer().isVerticalLabelsVisible   = true
//
//        graphData.sortBy { it.date }
//
//
//        if (graphData.size > 0){
//            if (firstValute?.valute == graphData[0].valute){
//
//                graphView.removeAllSeries()
//
//                var dataPoints = mutableListOf<DataPoint>()
//
//                for (valuteInfo in graphData){
//
//
//                    dataPoints.add(DataPoint(valuteInfo.date, valuteInfo.value))
//                }
//
//                var firstDate = Date()
//                var lastDate =  Date()
//
//                if (graphData.size > 0){
//                    firstDate = graphData[0].date
//                    lastDate = graphData[graphData.size-1].date
//                }
//
//
//
//                val series = LineGraphSeries(dataPoints.toTypedArray())
//
//
////                val seriesPoints = PointsGraphSeries<DataPoint>(dataPoints.toTypedArray())
////                seriesPoints.setCustomShape { canvas, paint, x, y, dataPoint ->
////                    paint.color = Color.BLACK
////                    paint.textSize = 20f
////                    canvas.drawText(dataPoint.y.toString(), x, y, paint)
////                }
//
//
//
//                series.setAnimated(true)
//                series.setDrawDataPoints(true)
//                series.setDataPointsRadius(15F)
//                series.setThickness(8)
//
//
//
//                series.setDrawBackground(true)
//                series.setDrawDataPoints(true)
//
//
//
//
//                graphView.addSeries(series)
//
//
//                graphView.getGridLabelRenderer().setLabelFormatter(object : DefaultLabelFormatter() {
//                    override fun formatLabel(value: Double, isValueX: Boolean): String {
//                        return if (isValueX) {
//                            sdf.format(value)
//                        } else {
////                            super.formatLabel(value, isValueX)
//                            precision.format(value)
//                        }
//                    }
//                })
//
//
//                graphView.getGridLabelRenderer().setNumHorizontalLabels(5)
//
//
//                graphView.getViewport().setMinX(firstDate.getTime().toDouble())
//                graphView.getViewport().setMaxX(lastDate.getTime().toDouble())
//                graphView.getViewport().setXAxisBoundsManual(false)
//
//                graphView.getViewport().setScalable(true)
////
//                graphView.getGridLabelRenderer().setHorizontalLabelsAngle(30)
//                graphView.getGridLabelRenderer().setHumanRounding(true)
//
////                graphView.getGridLabelRenderer().setVerticalLabelsColor(R.color.white);
////                graphView.getGridLabelRenderer().setHorizontalLabelsColor(R.color.white)
//
//
//
//                graphView.getGridLabelRenderer().reloadStyles()
//            }
//        }



    }

    fun calculateResult(){
        val sumValue = editTextSum.text.toString()

        var Res = getCalculatedResult(sumValue,firstValute,secondValute)

        tvResult.text = precision.format(Res)


        if (currTabPosition == 1) {
            replaceFragment(favorites_Converter_Fragment(firstValute,viewModel,sumValue,chosenDate))
//            replaceFragment(banksResultsFragment(firstValute,viewModel,editTextSum.text.toString()))
        } else if (currTabPosition == 2){
            replaceFragment(banksResultsFragment(firstValute,viewModel,sumValue))
        }



    }


      fun setValutePresentation(valuteInfo:ValuteInfo?,fieldToChange:TextView = chosenField){

          chosenImage.layoutParams.width = 0

//          Picasso.get().load(getValuteFlagPath(valuteInfo?.valute)).into(chosenImage)

          if (valuteInfo != null){
              fieldToChange.text = "${valuteInfo?.valute?.code}"
              Picasso.get().load(getValuteFlagPath(valuteInfo?.valute)).into(chosenImage)
              chosenImage.layoutParams.width = dpToPx(58,resources.displayMetrics.density)
              chosenImage.layoutParams.height= dpToPx(58,resources.displayMetrics.density)
          } else {
//              fieldToChange.text = "Select currency"
//              chosenImage.layoutParams.width = dpToPx(0,resources.displayMetrics.density)
//              chosenImage.layoutParams.height= dpToPx(0,resources.displayMetrics.density)
          }



//          chosenField.setCompoundDrawables(img.drawable,null,null,null)
      }




    fun onClickSwitcher(view: android.view.View) {
//        openValuteInfoListChooser()


        if (firstValute== null){
            return
        }

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
            "btn_remove" -> removeSymbol()
            else -> deleteSymbol()

        }
    }


    fun removeSymbol(){

        var positionStart = editTextSum.selectionStart
        var positionEnd = editTextSum.selectionEnd

        var inputText = editTextSum.text


        if (positionEnd ==0){
            return
        }

        if (positionStart==positionEnd){
            positionStart = positionEnd - 1
        }


        inputText.delete(positionStart,positionEnd)

//        symbolRemoved = true

    }


    fun performAction(actionSymbol:Char){

//        editTextSum.clearFocus()

        var positionStart = editTextSum.selectionStart
        var positionEnd = editTextSum.selectionEnd


//        if (!symbolRemoved){
//            editTextSum.clearFocus()
//            editTextSum.setSelection(editTextSum.text.length)

//        }


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

                if (sumValue.equals("")){
                    editTextSum.setText("0.00")
                } else {
                    editTextSum.setText(sumValue + ".")
                }
            }

        } else {







//            if (positionEnd ==0){
//                return
//            }

//            if (positionStart==positionEnd){
//                positionStart = positionEnd - 1
//            }

            var sumValue_new = sumValue.substring(0, positionStart) + number + sumValue.substring(positionEnd)


            editTextSum.setText(sumValue_new)

//            symbolRemoved = false





//            editTextSum.setText(sumValue + number)
        }


        if (editTextSum.text.toString().equals("00")){
            editTextSum.setText("0")
        }

        editTextSum.setSelection(editTextSum.text.length)
    }

    fun deleteSymbol(){



        editTextSum.clearFocus()

        dotIsPressed = false
        fraction = false
        additionalValue = false
        editTextSum.setText("0.00")
    }
}


