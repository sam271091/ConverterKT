package Apps.com.converterkt.pojo;


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "BankInfo")
data class BankInfo(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id :Integer ?=null ,
    @SerializedName("longDate")
    @Expose
     var date :Long ?=null,
    @SerializedName("bank")
    @Expose
    var  bank :String ?=null,
    @SerializedName("bankLogo")
    @Expose
    var  bankLogo : String ?=null,
    @SerializedName("currencyCode")
    @Expose
    var  currencyCode : String ?=null,
    @SerializedName("buy_Cash")
    @Expose
    var  buyCash : Double ?=null,
    @SerializedName("Buy_NonCash")
    @Expose
    var  buyNonCash : Double ?=null,
    @SerializedName("Buy_ForCards")
    @Expose
    var  buyForCards : Double ?=null,
    @SerializedName("Sell_Cash")
    @Expose
    var  sellCash : Double ?=null,
    @SerializedName("Sell_NonCash")
    @Expose
    var  sellNonCash : Double ?=null,
    @SerializedName("Sell_ForCards")
    @Expose
    var  sellForCards :Double ?=null){

//    @Ignore var children: List<BankInfo> = emptyList()
//
//    fun populateChildren(items: List<BankInfo>){
//        children = items.filter { it.bank == this.bank }
//        for (child in children)
//            child.populateChildren(items)
//    }

        }



