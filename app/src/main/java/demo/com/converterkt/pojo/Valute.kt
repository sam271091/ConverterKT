package demo.com.converterkt.pojo


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Valute",strict = false)
@Entity(tableName = "valute")
data class Valute @JvmOverloads constructor(
    @field:Attribute(name="Code")
    @PrimaryKey
    var code: String = "",
    @field:Element(name="Nominal")
    @Ignore
    var nominal : String = "",
    @field:Element(name="Name")
    var name : String = "",
    @field:Element(name="Value")
    @Ignore
    var value : Double = 0.0

) {
}