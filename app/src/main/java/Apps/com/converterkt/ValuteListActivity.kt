package Apps.com.converterkt

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import Apps.com.converterkt.adapters.ValuteInfoAdapter
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.pojo.ValuteInfo
import android.view.animation.AnimationUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_valute_list.*


class ValuteListActivity : AppCompatActivity() {
    private lateinit var viewModel : ConverterViewModel
    val adapter = ValuteInfoAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valute_list)


        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()
        adViewList.loadAd(adRequest)

        valuteRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]

        fillTheList()


        adapter.onValuteInfoClickListener = object : ValuteInfoAdapter.OnValuteInfoClickListener {
            override fun onClick(valuteInfo: ValuteInfo) {
                var intent = Intent()
                intent.putExtra("valuteInfo",valuteInfo)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }



        valuteRecyclerView.layoutManager = LinearLayoutManager(this)



        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null && newText?.length > 2){
                    filterList(newText)
                } else {
                    fillTheList()
                }


                return false
            }
        })



    }



    override fun onPause() {
        adViewList.pause()
        super.onPause()

    }

    override fun onResume() {
        adViewList.resume()
        super.onResume()
    }

    override fun onDestroy() {
        adViewList.destroy()
        super.onDestroy()
    }




    fun filterList(newText:String){

         viewModel.valutes("%" +newText + "%").observe(this, Observer {
             viewModel.getFilteredList(it as ArrayList<Valute>).observe(this, Observer {
                 adapter.valuteInfoList = it
             })
         })




    }

    fun fillTheList(){
        viewModel.allValuteInfo.observe(this, Observer {
            adapter.valuteInfoList = it
        })
    }

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context,ValuteListActivity::class.java)

            return intent
        }

    }

}