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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


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

                Spacer(modifier = Modifier
                    .height(4.dp)
//            .background(color = colorResource(id = R.color.white))
                )

//                Column(modifier = Modifier.weight(1f),
//                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorResource(id = R.color.white))
                        .border(
                            border = ButtonDefaults.outlinedBorder,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(top = 10.dp, bottom = 10.dp)
                        ,
                        verticalAlignment = Alignment.CenterVertically){
                        Column(modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()) {
                                Text(text = bankBranch.branchName.toString(),
                                fontWeight = FontWeight.SemiBold)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()) {
                                Text(text = bankBranch.vicinity.toString(),
                                fontStyle = FontStyle.Italic)
                            }
                        }
//                        Text(text = bankBranch.branchName.toString())
//                        Text(text = bankBranch.vicinity.toString())
                    }
//                }


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