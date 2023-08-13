package Apps.com.converterkt.pojo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull
import java.util.UUID

@Entity(tableName = "BankBranch")
data class BankBranch(
    @PrimaryKey(autoGenerate = true)
    var id :Integer ?=null,
    @SerializedName("bank")
    var bankCode : String? = "",
    @SerializedName("bankName")
    var branchName :String? = "",
    @SerializedName("address")
    var vicinity : String? ="",
    var lat :String? = "",
    var lng :String? = ""
)
