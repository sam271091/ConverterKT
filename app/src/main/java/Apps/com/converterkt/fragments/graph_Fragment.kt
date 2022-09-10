package Apps.com.converterkt.fragments

import Apps.com.converterkt.ConverterViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Apps.com.converterkt.R
import Apps.com.converterkt.pojo.ValuteInfo
import androidx.lifecycle.lifecycleScope
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_graph.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [graph_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class graph_Fragment(var currValuteInfo: ValuteInfo?, var viewModel : ConverterViewModel) : Fragment() {

//    private var param1: String? = null
//    private var param2: String? = null

    private var precision =  DecimalFormat("#,##0.0000")
    var sdf = SimpleDateFormat(" dd.MM.yy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        graphView.getGridLabelRenderer().isHorizontalLabelsVisible = false
        graphView.getGridLabelRenderer().isVerticalLabelsVisible   = false


        makeGraph(currValuteInfo)

        super.onViewCreated(view, savedInstanceState)
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
            if (currValuteInfo?.valute == graphData[0].valute){

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




//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment GraphFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            GraphFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}