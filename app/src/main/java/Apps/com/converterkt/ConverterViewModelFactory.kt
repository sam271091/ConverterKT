package Apps.com.converterkt

import Apps.com.converterkt.database.ConverterDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConverterViewModelFactory (
    private val converterDao: ConverterDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConverterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return null as T//ConverterViewModel(converterDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}