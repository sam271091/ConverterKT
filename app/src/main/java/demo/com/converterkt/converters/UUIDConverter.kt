package demo.com.converterkt.converters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

class UUIDConverter {

    @TypeConverter
    fun fromUUID(uuid:UUID?):String?{
        return uuid.let { it.toString() }
    }


    @TypeConverter
    fun uuidFromString(string:String?):UUID?{

        string.let {
            return UUID.fromString(string)
        }

        return UUID.fromString("00000000-0000-0000-0000-000000000000")

    }

}