package Apps.com.converterkt.fragments

import Apps.com.converterkt.ConverterViewModel
import Apps.com.converterkt.R
import Apps.com.converterkt.composeViewModel
import Apps.com.converterkt.pojo.BankInfo
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.pojo.ValuteInfo
import Apps.com.converterkt.utils.collectLatestLifecycleFlow
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import java.text.DecimalFormat


class banksResultsFragment(var currValuteInfo: ValuteInfo?, var converterviewModel : ConverterViewModel
                           , var value:String) : Fragment(){

    val viewModel = composeViewModel(converterviewModel)
    private var precision =  DecimalFormat("#,##0.00")
    val sumDouble : Double = when(value){
        ""->0.000
        else -> {
            value.toDouble()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed

//            var banksInfoByCurrency = converterviewModel.bankInfoByCurrency



            var valute = currValuteInfo?.let { it.valute }

            viewModel.searchQuery = valute?.code.toString()
            viewModel.getBanksDataByValute()

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {

                    Scaffold() { contentPadding ->
                        Box(
                            modifier = Modifier.padding(contentPadding)
                        ) {
                            // In Compose world
                            banksInfoByValute()
                        }
                    }


                }
            }
        }
    }




    @Composable
    fun banksInfoByValute(){

//        val state = viewModel.state




        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.white))
            ,
            ) {
            bankInfoByValuteListingCap()
            LazyColumn( modifier = Modifier.fillMaxSize(),
                userScrollEnabled = true
                ){
               items(viewModel.banksDataDetails.size){i->
                   var bankInfo = viewModel.banksDataDetails[i]
                   bankInfoItemByValute(bankInfo = bankInfo)
               }
           }
        }
        
    }
    @Composable
    fun bankInfoByValuteListingCap(){
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.labelTextColor))
            .padding(top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.bank_row_label),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold)
            }
            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.buycash_col_label),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold)
            }
            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.sellcash_col_label),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold)
            }
        }
    }

    @Composable
    fun bankInfoItemByValute(bankInfo:BankInfo){

        var buyCash = bankInfo?.buyCash ?: 0.0000
        var sellCash = bankInfo?.sellCash ?: 0.0000

        Spacer(modifier = Modifier
            .height(4.dp)
//            .background(color = colorResource(id = R.color.white))
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.white))
            .border(
                border = ButtonDefaults.outlinedBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(top = 10.dp, bottom = 10.dp)
            ,
        verticalAlignment = Alignment.CenterVertically) {


            Column(modifier = Modifier
                .weight(0.2f)
                ) {
                AsyncImage(model = bankInfo.bankLogo, contentDescription = "Bank logo")
            }


            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp)) {
                Text(text = bankInfo.bankName.toString(),
                color = colorResource(id = R.color.black))
            }
            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = precision.format(buyCash * sumDouble),
                    color = colorResource(id = R.color.black))
            }
            Column(modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = precision.format(sellCash * sumDouble),
                    color = colorResource(id = R.color.black))
            }
        }


    }


    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }
}