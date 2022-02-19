package Apps.com.converterkt

import Apps.com.converterkt.adapters.CurrencyItemDataAdapter
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.utils.dpToPx
import Apps.com.converterkt.utils.getValuteFlagPath
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_currency_item.*

class ActivityCurrencyItem : AppCompatActivity() {
    private lateinit var viewModel : ConverterViewModel
    val adapter = CurrencyItemDataAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_item)

        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]

      val currency = intent.getSerializableExtra("currency")

        fillTheData(currency as Valute?)



        rvValuteInfo.adapter = adapter


        rvValuteInfo.layoutManager = LinearLayoutManager(this)

    }


    fun fillTheData(currency:Valute?){
        Picasso.get().load(getValuteFlagPath(currency)).into(flagIV)
        flagIV.layoutParams.width = dpToPx(58,resources.displayMetrics.density)
        flagIV.layoutParams.height = dpToPx(84,resources.displayMetrics.density)
        codeTV.text = currency?.code
        nameTV.text = currency?.name

        if (currency != null){
            fillTheList(currency)
        }


    }

    fun fillTheList(currency:Valute){
        viewModel.getDataCurrencyItem(currency).observe(this, Observer {
            adapter.valuteInfoList = it
        })
    }

    companion object {
        fun newIntent(context:Context) : Intent{
            val intent = Intent(context,ActivityCurrencyItem::class.java)
            return intent
        }
    }
}