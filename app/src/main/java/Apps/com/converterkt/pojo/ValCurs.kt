package Apps.com.converterkt.pojo


import org.simpleframework.xml.Attribute
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root


@Root(name = "ValCurs")
data class ValCurs(
    @field:ElementList(name="ValType",inline = true)
    var valType : List<ValType>? = null,
    @field:Attribute(name="Date")
    var date : String? = "",
    @field:Attribute(name="Name")
    var name : String? = "",
    @field:Attribute(name="Description")
    var description : String? = ""
) {
}