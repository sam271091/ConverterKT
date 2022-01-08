package Apps.com.converterkt.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import Apps.com.converterkt.pojo.Valute

class ValuteConverter {

    @TypeConverter
    fun valuteToString(valute:Valute?):String?{
        return valute.let {
            it?.value = 0.0
            it?.nominal = "0.0"
            Gson().toJson(it)}
    }

    @TypeConverter
    fun stringToValute(valuteAsJson:String?):Valute?{
        return valuteAsJson.let { Gson().fromJson(it,Valute::class.java) }
    }
}