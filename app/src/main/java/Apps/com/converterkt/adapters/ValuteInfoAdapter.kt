package Apps.com.converterkt.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import Apps.com.converterkt.R
import Apps.com.converterkt.pojo.ValuteInfo
import Apps.com.converterkt.utils.getValuteFlagPath
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_valute_list.*
import kotlinx.android.synthetic.main.item_valute_info.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat


class ValuteInfoAdapter(context:Context):RecyclerView.Adapter<ValuteInfoAdapter.ValuteInfoViewHolder>() {

    var valuteInfoList : List<ValuteInfo> = listOf()


    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var onValuteInfoClickListener : OnValuteInfoClickListener? = null

    private var precision =  DecimalFormat("#,##0.0000")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValuteInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_valute_info,parent,false)

        return ValuteInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ValuteInfoViewHolder, position: Int) {
        val valuteInfo = valuteInfoList[position]

        with(holder){

            with(valuteInfo){
                currencyDate.text = SimpleDateFormat("dd.MM.yyyy").format(valuteInfo.date)
                tvCode.text = valuteInfo.valute?.code
                tvValuteFullName.text = valuteInfo.valute?.name
                tvValue.text = precision.format(valuteInfo.value)
                tvNominal.text = precision.format(valuteInfo.nominal)
                Picasso.get().load(getValuteFlagPath(valuteInfo.valute)).into(ivFlag)
                itemView.setOnClickListener {
                    onValuteInfoClickListener?.onClick(this)
                }


            }

        }


    }



    override fun getItemCount(): Int {
        return valuteInfoList.size
    }

    inner class ValuteInfoViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val ivFlag = itemView.ivFlag
        val currencyDate = itemView.currencyDate
        val tvCode = itemView.tvCode
        val tvValuteFullName = itemView.tvValuteFullName
        val tvValue = itemView.tvValue
        val tvNominal = itemView.tvNominal
    }

    interface OnValuteInfoClickListener{
        fun onClick(valuteInfo:ValuteInfo)
    }
}