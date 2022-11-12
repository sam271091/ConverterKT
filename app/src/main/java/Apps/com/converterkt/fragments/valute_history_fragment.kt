package Apps.com.converterkt.fragments

import Apps.com.converterkt.ConverterViewModel
import Apps.com.converterkt.R
import Apps.com.converterkt.adapters.CurrencyItemDataAdapter
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.utils.collectLatestLifecycleFlow
import Apps.com.converterkt.utils.dpToPx
import Apps.com.converterkt.utils.getValuteFlagPath
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_currency_item.*
import kotlinx.android.synthetic.main.valute_history_fragment.*
import java.io.Serializable
import java.util.*

class valute_history_fragment(val currency:Serializable?,var viewModel : ConverterViewModel, val adapter:CurrencyItemDataAdapter) : Fragment() {


    //    private lateinit var viewModel : ConverterViewModel
//    val adapter = context?.let { CurrencyItemDataAdapter(it) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.valute_history_fragment,container,false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]

        rvValuteInfo.adapter = adapter

        fillTheData(currency as Valute?)

        rvValuteInfo.layoutManager = LinearLayoutManager(context)


        floatingScrollUp.setOnClickListener {
            rvValuteInfo.scrollToPosition(0)
            floatingScrollUp.isVisible = false
            floatingScrollDown.isVisible = true
        }

        floatingScrollDown.setOnClickListener {
            rvValuteInfo.scrollToPosition((adapter?.itemCount ?: 0) -1)
            floatingScrollDown.isVisible = false
            floatingScrollUp.isVisible = true

        }


        rvValuteInfo.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState){
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        var linearLayoutManager =  recyclerView.layoutManager as LinearLayoutManager
                        var currentItem = linearLayoutManager.findFirstVisibleItemPosition()
//                        Log.v("scroll_A", "currentItem $currentItem")
                        floatingScrollUp.isVisible = currentItem != 0


                        floatingScrollDown.isVisible = currentItem != ((adapter?.itemCount ?: 0) -1) - 4
                    }
                }

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    //        rvValuteInfo.adapter = adapter
//
//        fillTheData(currency as Valute?)
//
//        rvValuteInfo.layoutManager = LinearLayoutManager(this)
//
//
//        floatingScrollUp.setOnClickListener {
//            rvValuteInfo.scrollToPosition(0)
//            floatingScrollUp.isVisible = false
//            floatingScrollDown.isVisible = true
//        }
//
//        floatingScrollDown.setOnClickListener {
//            rvValuteInfo.scrollToPosition(adapter.itemCount-1)
//            floatingScrollDown.isVisible = false
//            floatingScrollUp.isVisible = true
//
//        }
//
//
//        rvValuteInfo.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                when (newState){
//                    RecyclerView.SCROLL_STATE_IDLE -> {
//                        var linearLayoutManager =  recyclerView.layoutManager as LinearLayoutManager
//                        var currentItem = linearLayoutManager.findFirstVisibleItemPosition()
////                        Log.v("scroll_A", "currentItem $currentItem")
//                        floatingScrollUp.isVisible = currentItem != 0
//
//
//                        floatingScrollDown.isVisible = currentItem != (adapter.itemCount-1) - 4
//                    }
//                }
//
//            }

//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//            }
//        })


        fun fillTheData(currency:Valute?){
//        Picasso.get().load(getValuteFlagPath(currency)).into(flagIV)
//        flagIV.layoutParams.width = dpToPx(58,resources.displayMetrics.density)
//        flagIV.layoutParams.height = dpToPx(84,resources.displayMetrics.density)
//        codeTV.text = currency?.code
//        nameTV.text = currency?.name

        if (currency != null){
            fillTheList(currency)
        }


    }

    fun fillTheList(currency:Valute){
//        viewModel.getDataCurrencyItem(currency).observe(viewLifecycleOwner, Observer {
//            adapter?.valuteInfoList = it
//            rvValuteInfo.scrollToPosition(adapter?.itemCount?.minus(1) ?: 0)
//            floatingScrollDown.isVisible = false
//        })

        collectLatestLifecycleFlow(viewModel.getDataCurrencyItem(currency)){
            adapter?.valuteInfoList = it
            rvValuteInfo.scrollToPosition(adapter?.itemCount?.minus(1) ?: 0)
            floatingScrollDown.isVisible = false
        }

    }

}





