package Apps.com.converterkt

import Apps.com.converterkt.pojo.BankInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import Apps.com.converterkt.ui.theme.ConverterKTTheme
import android.content.Context
import android.content.Intent

class BankBranchActivity() : ComponentActivity() {

    private val bankInfo:BankInfo by lazy {
        intent?.getSerializableExtra("bankInfo") as BankInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConverterKTTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Hello, ${bankInfo.bank}")
                }
            }
        }
    }

    companion object {

        fun newIntent(context: Context,bankInfo: BankInfo) : Intent {
            val intent = Intent(context,BankBranchActivity::class.java).apply {
                putExtra("bankInfo",bankInfo)
            }

            return intent
        }

    }
}






@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ConverterKTTheme {
        Greeting("Android")
    }
}