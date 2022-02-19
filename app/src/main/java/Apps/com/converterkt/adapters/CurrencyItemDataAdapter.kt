package Apps.com.converterkt.adapters

import Apps.com.converterkt.R
import Apps.com.converterkt.pojo.ValuteInfo
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.currency_info_item.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class CurrencyItemDataAdapter(context:Context): RecyclerView.Adapter<CurrencyItemDataAdapter.currencyItemDataViewHolder>() {

    var valuteInfoList : List<ValuteInfo> = listOf()

    private var precision =  DecimalFormat("#,##0.0000")

        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): currencyItemDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.currency_info_item,parent,false)

        return currencyItemDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: currencyItemDataViewHolder, position: Int) {
        val valuteInfo = valuteInfoList[position]

        with(holder){

            with(valuteInfo){
                dateTV.text  = SimpleDateFormat("dd.MM.yyyy").format(valuteInfo.date)
                nominalTV.text = precision.format(nominal)
                valueTV.text = precision.format(value)
            }

        }
    }

    override fun getItemCount(): Int {
        return valuteInfoList.size
    }

    inner class currencyItemDataViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

        val dateTV = itemView.dateTV
        val nominalTV = itemView.nominalTV
        val valueTV = itemView.valueTV

    }
}