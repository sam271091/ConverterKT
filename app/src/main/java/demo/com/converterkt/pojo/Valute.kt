package demo.com.converterkt.pojo


import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Valute",strict = false)
data class Valute @JvmOverloads constructor(
    @field:Attribute(name="Code")
    var code: String = "",
    @field:Element(name="Nominal")
    var nominal : String = "",
    @field:Element(name="Name")
    var name : String = "",
    @field:Element(name="Value")
    var value : Double = 0.0

) {
}