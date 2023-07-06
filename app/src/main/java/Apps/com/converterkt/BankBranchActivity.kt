package Apps.com.converterkt

import Apps.com.converterkt.composeScreens.BankBranchScreen
import Apps.com.converterkt.composeScreens.BankBranchesMapScreen
import Apps.com.converterkt.pojo.BankInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import Apps.com.converterkt.ui.theme.ConverterKTTheme
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider

class BankBranchActivity() : ComponentActivity() {

    private val bankInfo:BankInfo by lazy {
        intent?.getSerializableExtra("bankInfo") as BankInfo
    }



    private lateinit var converterviewModel : ConverterViewModel

    private lateinit var viewModel : composeViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        converterviewModel = ViewModelProvider(this)[ConverterViewModel::class.java]

        viewModel = composeViewModel(converterviewModel)

        setContent {
            ConverterKTTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    Greeting("Hello, ${bankInfo.bank}")
//                    BankBranchScreen(viewModel = viewModel, bankInfo = bankInfo)
                    TabScreen()
                }
            }
        }
    }

    @Composable
    fun TabScreen() {
        var tabIndex by remember { mutableStateOf(0) }

        val tabs = listOf(stringResource(R.string.bank_branches_list), stringResource(R.string.Map))

        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(selectedTabIndex = tabIndex, backgroundColor = colorResource(id = R.color.custom_color_background)) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = { Text(title,color = colorResource(id = R.color.mainwhite)) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabIndex) {
                0 -> BankBranchScreen(viewModel = viewModel, bankInfo = bankInfo)
                1 -> BankBranchesMapScreen(viewModel = viewModel)
//                2 -> SettingsScreen()
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