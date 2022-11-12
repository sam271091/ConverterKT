package Apps.com.converterkt.fragments

import Apps.com.converterkt.ConverterViewModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Apps.com.converterkt.R
import Apps.com.converterkt.adapters.FavoriteAdapter
import Apps.com.converterkt.adapters.ValuteInfoAdapter
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.pojo.ValuteInfo
import Apps.com.converterkt.utils.collectLatestLifecycleFlow
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_valute_list.*
import kotlinx.android.synthetic.main.fragment_favorites__converter_.*
import kotlinx.android.synthetic.main.fragment_graph.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class favorites_Converter_Fragment(var currValuteInfo: ValuteInfo?, var viewModel : ConverterViewModel
,var value:String,var chosenDate:Date) : Fragment() {


    lateinit var adapter: FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = FavoriteAdapter(view.context)



//        CoroutineScope(Dispatchers.IO).launch {
//            var favValutes = viewModel.getFavouriteValutesForFilter()

//            viewModel.getFilteredList(favValutes as ArrayList<Valute>,chosenDate).observe(viewLifecycleOwner,
//            Observer {
//                adapter.valuteInfoList = it
//            })

//            var filteredList = viewModel.getFilteredListNonObservable(favValutes as ArrayList<Valute>,chosenDate)
//
//            adapter.valuteInfoList = filteredList

//            viewModel.favoriteValutes.observe(viewLifecycleOwner, Observer {
//                viewModel.getFilteredList(it as ArrayList<Valute>,chosenDate).observe(viewLifecycleOwner, Observer {
//
//                    noFavsLabel.isVisible = it.size == 0
//
//                    adapter.valuteInfoList = it
//                    adapter.firstValute = currValuteInfo
//                    adapter.currValue  = value
//
//
//                })
//            })

//        }

        collectLatestLifecycleFlow(viewModel.favoriteValutes){filter->
            collectLatestLifecycleFlow(viewModel.getFilteredList(filter as ArrayList<Valute>,chosenDate)){
                noFavsLabel.isVisible = it.size == 0

                adapter.valuteInfoList = it
                adapter.firstValute = currValuteInfo
                adapter.currValue  = value
            }

        }


        rvFavoritesConverter.adapter = adapter


        rvFavoritesConverter.layoutManager = LinearLayoutManager(view.context)

        super.onViewCreated(view, savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites__converter_, container, false)
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment favorites_Converter_Fragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            favorites_Converter_Fragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}