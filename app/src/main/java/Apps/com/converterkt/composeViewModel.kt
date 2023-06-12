package Apps.com.converterkt

import Apps.com.converterkt.pojo.BankInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch



class composeViewModel(var converterviewModel : ConverterViewModel) : ViewModel() {
//    var state by mutableStateOf(BankInfoState())
      var banksDataDetails by mutableStateOf(listOf<BankInfo>())
      var searchQuery by mutableStateOf("")


//    lateinit var converterviewModel : ConverterViewModel

//    init {
//        getBanksDataByValute()
//    }

     fun getBanksDataByValute(){
        viewModelScope.launch {
            converterviewModel.banksDataByValute(searchQuery.toString())
                .collect{result ->
//                    state.banksDataDetails = result
                    banksDataDetails = result

//                    state = state.copy()
                }
        }
    }

}