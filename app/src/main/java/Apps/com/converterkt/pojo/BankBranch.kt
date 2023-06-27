package Apps.com.converterkt.pojo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.UUID

@Entity(tableName = "BankBranch")
data class BankBranch(
    @PrimaryKey(autoGenerate = true)
    var id :Integer ?=null,
    var bankCode : String? = "",
    var branchName :String? = "",
    var vicinity : String? ="",
    var lat :String? = "",
    var lng :String? = ""
)
