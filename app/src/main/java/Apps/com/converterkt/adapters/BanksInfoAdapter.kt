package Apps.com.converterkt.adapters

import Apps.com.converterkt.pojo.BankInfo
import Apps.com.converterkt.pojo.ValuteInfo
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BanksInfoAdapter(context:Context):RecyclerView.Adapter<BanksInfoAdapter.BanksInfoViewHolder>() {

    var banksDataList : List<BankInfo> = listOf()

        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var banksInfoList : List<BankInfo> = listOf()

        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BanksInfoViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BanksInfoViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class BanksInfoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

    }
}