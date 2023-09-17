package Apps.com.converterkt

import Apps.com.converterkt.pojo.BankBranch
import Apps.com.converterkt.pojo.BankInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch



class composeViewModel(var converterviewModel : ConverterViewModel) : ViewModel(),java.io.Serializable {
//    var state by mutableStateOf(BankInfoState())
      var banksDataDetails by mutableStateOf(listOf<BankInfo>())
      var searchQuery by mutableStateOf("")
      var bankName    by mutableStateOf("")
      var bankBranches by mutableStateOf(listOf<BankBranch>())
      var buyCash_enabled by  mutableStateOf(true)
      var sellCash_enabled by  mutableStateOf(true)


//    lateinit var converterviewModel : ConverterViewModel

//    init {
//        getBanksDataByValute()
//    }

     fun getBanksDataByValute(){
        viewModelScope.launch {
            converterviewModel.banksDataByValute(searchQuery.toString(),buyCash_enabled,sellCash_enabled)
                .collect{result ->
//                    state.banksDataDetails = result
                    banksDataDetails = result

//                    state = state.copy()
                }
        }
    }

    fun getBanksDataByValuteSortBuyCash(){
        viewModelScope.launch {
            converterviewModel.banksDataByValuteSortBuyCash(searchQuery.toString(),buyCash_enabled)
                .collect{result ->
//                    state.banksDataDetails = result
                    banksDataDetails = result

//                    state = state.copy()
                }
        }
    }

    fun getBanksDataByValuteSortSellCash(){
        viewModelScope.launch {
            converterviewModel.banksDataByValuteSortSellCash(searchQuery.toString(),sellCash_enabled)
                .collect{result ->
//                    state.banksDataDetails = result
                    banksDataDetails = result

//                    state = state.copy()
                }
        }
    }


    fun getBankBranchByBank(){
        viewModelScope.launch {
            converterviewModel.bankBranchByBank(bankName.toString())
                .collect{result ->
                    bankBranches = result

                }
        }
    }

}