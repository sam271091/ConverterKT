package demo.com.converterkt.pojo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "valuteInfo")
data class ValuteInfo (
    var date: Date,
    var valute:Valute?=null,
    @PrimaryKey
    @NonNull
    var id: String = SimpleDateFormat("dd.MM.yyyy").format(date) + " ${valute?.code}",
    var nominal : Double = 0.0,
    var value   : Double = 0.0
) : Serializable{




}