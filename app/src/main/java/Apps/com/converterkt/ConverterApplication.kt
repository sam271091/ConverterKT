package Apps.com.converterkt

import Apps.com.converterkt.database.AppDatabase
import android.app.Application

class ConverterApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this)}
    }