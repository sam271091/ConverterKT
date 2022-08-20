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
import Apps.com.converterkt.pojo.FavoriteValute
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.pojo.ValuteInfo
import android.view.animation.AnimationUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_valute_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ValuteListActivity : AppCompatActivity() {
    private lateinit var viewModel : ConverterViewModel
    val adapter = ValuteInfoAdapter(this)
    lateinit var chosenDate : Date
    var favValutes = listOf<FavoriteValute?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valute_list)


        MobileAds.initialize(this)

        val adRequest = AdRequest.Builder().build()
        adViewList.loadAd(adRequest)

        valuteRecyclerView.adapter = adapter






        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]


        CoroutineScope(Dispatchers.IO).launch {
            favValutes = viewModel.getFavoriteValutes()
        }



        var chosenDateLong = intent.getSerializableExtra("chosenDate") as Long
//        var calInstance = Calendar.getInstance()
//        calInstance.time = chosenDateLong
        chosenDate = Date(chosenDateLong)


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

        searchBox.setOnClickListener {
            searchBox.isIconified = false
            searchBox.setIconifiedByDefault(false)
        }

        searchBox.setOnCloseListener {
            return@setOnCloseListener false
        }




        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null && newText?.length > 0){
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
             viewModel.getFilteredList(it as ArrayList<Valute>,chosenDate).observe(this, Observer {
                 adapter.valuteInfoList = it
                 adapter.favValutes = favValutes
             })
         })




    }

    fun fillTheList(){
        viewModel.allValuteInfo(chosenDate).observe(this, Observer {
            adapter.valuteInfoList = it
            adapter.favValutes = favValutes
        })
    }

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context,ValuteListActivity::class.java)

            return intent
        }

    }

}