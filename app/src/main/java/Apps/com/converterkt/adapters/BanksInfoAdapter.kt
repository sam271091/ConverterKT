package Apps.com.converterkt.adapters

import Apps.com.converterkt.R
import Apps.com.converterkt.pojo.BankInfo
import Apps.com.converterkt.utils.dpToPx
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentViewHolder
import com.squareup.picasso.Picasso
import de.codecrafters.tableview.TableDataAdapter
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import kotlinx.android.synthetic.main.item_bank_info.view.*

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bank_info,parent,false)

        return BanksInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: BanksInfoViewHolder, position: Int) {
        val bankData = banksDataList.get(position)

        with(holder){
            with(bankData){
                Picasso.get().load(bankData.bankLogo).into(iVBankLogo)
                tvBankName.setText(bankData.bankName)



                val list: MutableList<Array<String?>> = ArrayList()

                val valutelist: MutableList<String?> = ArrayList<String?>()


                var banksDetails = banksInfoList.filter { it.bank.equals(bankData.bank)}

                for (banksDetailsVi in banksDetails) {
                    val records = arrayOfNulls<String>(5)
                    if (banksDetailsVi.currencyCode == null) {
                        records[0] = "None"
                    } else {
                        records[0] = banksDetailsVi.currencyCode
                    }
                    valutelist.add(banksDetailsVi.currencyCode)
                    records[1] = banksDetailsVi.buyCash?.let { java.lang.Double.toString(it) }
                    records[2] = banksDetailsVi.sellCash?.let { java.lang.Double.toString(it) }
                    records[3] = banksDetailsVi.buyNonCash?.let { java.lang.Double.toString(it) }
                    records[4] = banksDetailsVi.sellNonCash?.let { java.lang.Double.toString(it) }
                    list.add(records)
                }



                val tableView =
                    holder.itemView.findViewById<View>(R.id.tableView) as TableView<Array<String>>

                createTable(tableView,holder.itemView.context,list)


            }

        }
    }


    fun createTable(tableView: TableView<*>?, context: Context?, tableData:MutableList<Array<String?>>) {
//        reportsGenerator.setTableData(tableData)
//        reportsGenerator.createTable(tableView, context, holder.itemView)

        tableView!!.columnCount = 5

        tableView!!.setHeaderBackgroundColor(Color.parseColor("#2ecc71"))

        val Columns = arrayOf("Valute","buy Cash","sell Cash","buy non-Cash","sell non-Cash")


        tableView!!.headerAdapter = SimpleTableHeaderAdapter(context, *Columns)
        val tableDataAdapter: TableDataAdapter<*> = SimpleTableDataAdapter(context, tableData)
        tableView!!.dataAdapter = tableDataAdapter

        var height = tableData.size * 55;

        tableView.layoutParams.height= context?.resources?.displayMetrics?.let {
            dpToPx(height,
                it?.density)
        }!!

    }

    override fun getItemCount(): Int {
        return banksDataList.size
    }

    inner class BanksInfoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val tvBankName = itemView.textViewBankName
        val iVBankLogo = itemView.iVBankLogo
    }
}