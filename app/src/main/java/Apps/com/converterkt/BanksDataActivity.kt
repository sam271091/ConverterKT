package Apps.com.converterkt

import Apps.com.converterkt.adapters.BanksInfoAdapter
import Apps.com.converterkt.adapters.ValuteInfoAdapter
import Apps.com.converterkt.pojo.BankInfo
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_banks_data.*
import kotlinx.android.synthetic.main.activity_valute_list.*

class BanksDataActivity : AppCompatActivity() {
    private lateinit var viewModel : ConverterViewModel
    val adapter = BanksInfoAdapter(this)
    var dataMap : HashMap<String, List<BankInfo> > = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banks_data)

        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()
        adViewListBanksData.loadAd(adRequest)


        banksInfoRV.adapter = adapter

        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]


        banksInfoRV.layoutManager = LinearLayoutManager(this)




        viewModel.allBanksInfo.observe(this, Observer {
            adapter.banksInfoList = it
//            dataMap.put("banksInfoList",it)
//            adapter.dataMap = dataMap
        })


        viewModel.allBanksData.observe(this, Observer {
            adapter.banksDataList = it
//            dataMap.put("banksInfoList",it)
        })


//        viewModel.allBanksData.observe(this, Observer {
////            adapter.banksDataList = it
//
//            dataMap.put("banksInfoList",it)
//
//            viewModel.allBanksInfo.observe(this, Observer {
////            adapter.banksInfoList = it
//                dataMap.put("banksInfoList",it)
//                adapter.dataMap = dataMap
//            })
//
//
//        })



    }






    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context,BanksDataActivity::class.java)

            return intent
        }

    }
}