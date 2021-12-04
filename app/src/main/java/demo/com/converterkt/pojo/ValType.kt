package demo.com.converterkt.pojo


import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ValType")
data class ValType(
    @field:Attribute(name = "Type")
    var type : String? = "",

    @field:ElementList(name = "Valute",inline = true)
    var valute : List<Valute>? = null


) {
}