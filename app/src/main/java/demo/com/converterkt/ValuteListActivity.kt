package demo.com.converterkt

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import demo.com.converterkt.adapters.ValuteInfoAdapter
import demo.com.converterkt.pojo.Valute
import demo.com.converterkt.pojo.ValuteInfo
import kotlinx.android.synthetic.main.activity_valute_list.*


class ValuteListActivity : AppCompatActivity() {
    private lateinit var viewModel : ConverterViewModel
    val adapter = ValuteInfoAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valute_list)



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