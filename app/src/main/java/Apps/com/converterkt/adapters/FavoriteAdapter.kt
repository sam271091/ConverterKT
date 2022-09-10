package Apps.com.converterkt.adapters

import Apps.com.converterkt.R
import Apps.com.converterkt.pojo.ValuteInfo
import Apps.com.converterkt.utils.getCalculatedResult
import Apps.com.converterkt.utils.getValuteFlagPath
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_valute_info.view.*
import kotlinx.android.synthetic.main.item_valute_info.view.ivFlag
import kotlinx.android.synthetic.main.item_valute_info.view.tvCode
import kotlinx.android.synthetic.main.item_valute_info.view.tvValuteFullName
import kotlinx.android.synthetic.main.row_item_favorite.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class FavoriteAdapter(context: Context):RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var currValue = "0.0000"
    var firstValute : ValuteInfo? = null
    var valuteInfoList : List<ValuteInfo> = listOf()


        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var precision =  DecimalFormat("#,##0.0000")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item_favorite,parent,false)

        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val valuteInfo = valuteInfoList[position]

        with(holder){

            with(valuteInfo){
                tvCode.text = valuteInfo.valute?.code
                tvValuteFullName.text = valuteInfo.valute?.name

                var Res = getCalculatedResult(currValue,firstValute,valuteInfo)


                tvValue.text = precision.format(Res)
                Picasso.get().load(getValuteFlagPath(valuteInfo.valute)).into(ivFlag)

            }

        }
    }

    override fun getItemCount(): Int {
        return valuteInfoList.size
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivFlag = itemView.ivFlag
        val tvCode = itemView.tvCode
        val tvValuteFullName = itemView.tvValuteFullName
        val tvValue = itemView.tvFavValue
    }
}