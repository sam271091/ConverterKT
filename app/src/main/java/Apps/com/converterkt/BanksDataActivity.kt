package Apps.com.converterkt

import Apps.com.converterkt.adapters.BanksInfoAdapter
import Apps.com.converterkt.adapters.ValuteInfoAdapter
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_valute_list.*

class BanksDataActivity : AppCompatActivity() {
    private lateinit var viewModel : ConverterViewModel
    val adapter = BanksInfoAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banks_data)

//        MobileAds.initialize(this)
//
//        val adRequest = AdRequest.Builder().build()
//        adViewList.loadAd(adRequest)


        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]


        viewModel.allBanksInfo.observe(this, Observer {
            adapter.banksInfoList = it
        })


        viewModel.allBanksData.observe(this, Observer {
            adapter.banksDataList = it
        })


    }






    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context,BanksDataActivity::class.java)

            return intent
        }

    }
}