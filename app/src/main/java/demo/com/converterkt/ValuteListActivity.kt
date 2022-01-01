package demo.com.converterkt

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import demo.com.converterkt.adapters.ValuteInfoAdapter
import demo.com.converterkt.pojo.ValuteInfo
import kotlinx.android.synthetic.main.activity_valute_list.*


class ValuteListActivity : AppCompatActivity() {
    private lateinit var viewModel : ConverterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_valute_list)

        val adapter = ValuteInfoAdapter(this)

        valuteRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]
        viewModel.allValuteInfo.observe(this, Observer {
            adapter.valuteInfoList = it
        })


        adapter.onValuteInfoClickListener = object : ValuteInfoAdapter.OnValuteInfoClickListener {
            override fun onClick(valuteInfo: ValuteInfo) {
                var intent = Intent()
                intent.putExtra("valuteInfo",valuteInfo)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }



        valuteRecyclerView.layoutManager = LinearLayoutManager(this)


    }

    companion object {

        fun newIntent(context: Context) : Intent {
            val intent = Intent(context,ValuteListActivity::class.java)

            return intent
        }

    }

}