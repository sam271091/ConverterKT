package Apps.com.converterkt

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import Apps.com.converterkt.pojo.ValuteInfo

class MySecondActivityContract : ActivityResultContract<Intent, ValuteInfo?>() {

    override fun createIntent(context: Context, input: Intent?): Intent {
        return Intent(context, ValuteListActivity::class.java)
            .putExtra("my_input_key", input)
            .putExtra("chosenDate",input?.getSerializableExtra("chosenDate"))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ValuteInfo? {
        return intent?.getSerializableExtra("valuteInfo") as ValuteInfo?
    }

    override fun getSynchronousResult(
        context: Context,
        input: Intent?
    ): SynchronousResult<ValuteInfo?>? {
        return super.getSynchronousResult(context, input)
    }
}