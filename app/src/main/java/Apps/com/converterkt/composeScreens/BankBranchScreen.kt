package Apps.com.converterkt.composeScreens

import Apps.com.converterkt.Greeting
import Apps.com.converterkt.R
import Apps.com.converterkt.composeViewModel
import Apps.com.converterkt.pojo.BankInfo
import Apps.com.converterkt.ui.theme.ConverterKTTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource


@Composable
fun BankBranchScreen(viewModel:composeViewModel,
                     bankInfo: BankInfo) {

    viewModel.bankName = bankInfo.bank.toString()
    viewModel.getBankBranchByBank()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.white))
        ,
    ) {

        LazyColumn( modifier = Modifier.fillMaxSize(),
            userScrollEnabled = true
        ){
            items(viewModel.bankBranches.size){i->
                var bankBranch = viewModel.bankBranches[i]

                Text(text = bankBranch.branchName.toString())

            }
        }
    }

}

//class BankBranchActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ConverterKTTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
//    }
//}