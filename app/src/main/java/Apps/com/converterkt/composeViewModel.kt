package Apps.com.converterkt

import Apps.com.converterkt.pojo.BankInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class composeViewModel : ViewModel() {
    var state by mutableStateOf(BankInfoState())
//    var banksDataDetails by mutableStateListOf<BankInfo>()
}