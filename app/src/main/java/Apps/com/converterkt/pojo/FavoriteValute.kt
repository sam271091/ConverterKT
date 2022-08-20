package Apps.com.converterkt.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.simpleframework.xml.Root


@Entity(tableName = "favoriteValutes")
data class FavoriteValute @JvmOverloads constructor(
    var valute:Valute?=null,
    @PrimaryKey
    var code: String = valute?.let {it.code}.toString()

) {

}