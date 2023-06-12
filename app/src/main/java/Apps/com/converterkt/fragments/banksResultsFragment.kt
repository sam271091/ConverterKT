package Apps.com.converterkt.fragments

import Apps.com.converterkt.ConverterViewModel
import Apps.com.converterkt.composeViewModel
import Apps.com.converterkt.pojo.BankInfo
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.pojo.ValuteInfo
import Apps.com.converterkt.utils.collectLatestLifecycleFlow
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel

class banksResultsFragment(var currValuteInfo: ValuteInfo?, var converterviewModel : ConverterViewModel
                           , var value:String) : Fragment(){

    val viewModel = composeViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed

//            var banksInfoByCurrency = converterviewModel.bankInfoByCurrency

            var valute = currValuteInfo?.let { it.valute }

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {


//

                    collectLatestLifecycleFlow(converterviewModel.banksDataByValute(valute?.code.toString())){
                       var banksDataDetails = it.filter { it.buyCash != 0.0000 && it.sellCash != 0.0000 }
                        viewModel.state.banksDataDetails = banksDataDetails

                    }

//                    val viewModel = viewModel<composeViewModel>()

                    // In Compose world
                    banksInfoByValute()
                }
            }
        }
    }


    @Composable
    fun banksInfoByValute(){

        Column(modifier = Modifier.fillMaxSize()) {
           LazyColumn( modifier = Modifier.fillMaxSize()){
               items(viewModel.state.banksDataDetails.size){i->
                   var bankInfo = viewModel.state.banksDataDetails[i]
                   bankInfoItemByValute(bankInfo = bankInfo)
               }
           }
        }
        
    }

    @Composable
    fun bankInfoItemByValute(bankInfo:BankInfo){
        Text(text = bankInfo.bankName.toString())
    }


    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }
}