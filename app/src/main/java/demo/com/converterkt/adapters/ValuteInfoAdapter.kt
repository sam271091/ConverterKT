package demo.com.converterkt.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import demo.com.converterkt.R
import demo.com.converterkt.pojo.ValuteInfo
import demo.com.converterkt.utils.getValuteFlagPath
import kotlinx.android.synthetic.main.item_valute_info.view.*
import java.text.DecimalFormat


class ValuteInfoAdapter(context:Context):RecyclerView.Adapter<ValuteInfoAdapter.ValuteInfoViewHolder>() {

    var valuteInfoList : List<ValuteInfo> = listOf()

    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var onValuteInfoClickListener : OnValuteInfoClickListener? = null

    var precision =  DecimalFormat("#,##0.0000")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValuteInfoViewHolder {
        val View = LayoutInflater.from(parent.context).inflate(R.layout.item_valute_info,parent,false)

        return ValuteInfoViewHolder(View)
    }

    override fun onBindViewHolder(holder: ValuteInfoViewHolder, position: Int) {
        val valuteInfo = valuteInfoList[position]

        with(holder){

            with(valuteInfo){
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
        val tvCode = itemView.tvCode
        val tvValuteFullName = itemView.tvValuteFullName
        val tvValue = itemView.tvValue
        val tvNominal = itemView.tvNominal
    }

    interface OnValuteInfoClickListener{
        fun onClick(valuteInfo:ValuteInfo)
    }
}