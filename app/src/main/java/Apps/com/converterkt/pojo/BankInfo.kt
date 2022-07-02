package Apps.com.converterkt.pojo;


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "BankInfo")
data class BankInfo(
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id :Integer,
    @SerializedName("date")
    @Expose
     var date :String,
    @SerializedName("bank")
    @Expose
    var  bank :String,
    @SerializedName("bankLogo")
    @Expose
    var  bankLogo : String,
    @SerializedName("currencyCode")
    @Expose
    var  currencyCode : String,
    @SerializedName("buy_Cash")
    @Expose
    var  buyCash : Double,
    @SerializedName("Buy_NonCash")
    @Expose
    var  buyNonCash : Double,
    @SerializedName("Buy_ForCards")
    @Expose
    var  buyForCards : Double,
    @SerializedName("Sell_Cash")
    @Expose
    var  sellCash : Double,
    @SerializedName("Sell_NonCash")
    @Expose
    var  sellNonCash : Double,
    @SerializedName("Sell_ForCards")
    @Expose
    var  sellForCards :Double){

        }



