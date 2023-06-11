package Apps.com.converterkt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class composeViewModel : ViewModel() {
    var value by mutableStateOf("")
}