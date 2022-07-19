package Apps.com.converterkt.adapters

import Apps.com.converterkt.R
import Apps.com.converterkt.pojo.BankInfo
import Apps.com.converterkt.utils.dpToPx
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.codecrafters.tableview.TableDataAdapter
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import kotlinx.android.synthetic.main.item_bank_info.view.*
import java.text.DecimalFormat

class BanksInfoAdapter(context:Context):RecyclerView.Adapter<BanksInfoAdapter.BanksInfoViewHolder>() {

    private var precision =  DecimalFormat("#,##0.0000")

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

//    var dataMap : HashMap<String, List<BankInfo>> = hashMapOf()
//
//        set(value) {
//            field = value
//            dataMap.get("banksDataList").let {
//                if (it != null) {
//                    banksDataList = it
//                }
//            }
//
//            dataMap.get("banksInfoList").let {
//                if (it != null) {
//                    banksInfoList = it
//                }
//            }
//
//
////            banksInfoList = dataMap.get("banksInfoList") as List<BankInfo>
//            notifyDataSetChanged()
//        }

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


                var banksDetails = banksInfoList.filter { it.bank.equals(bankData.bank)}

                createTableData(banksDetails,holder)


            }

        }
    }


    fun createTableData(banksDetails:List<BankInfo>,holder:BanksInfoViewHolder){
        val list: MutableList<Array<String?>> = ArrayList()

        val valutelist: MutableList<String?> = ArrayList<String?>()




        for (banksDetailsVi in banksDetails) {
            val records = arrayOfNulls<String>(3)
            if (banksDetailsVi.currencyCode == null) {
                records[0] = "None"
            } else {
                records[0] = banksDetailsVi.currencyCode
            }
            valutelist.add(banksDetailsVi.currencyCode)
//            records[1] = banksDetailsVi.buyCash?.let { java.lang.Double.toString(it) }
//            records[2] = banksDetailsVi.sellCash?.let { java.lang.Double.toString(it) }
//            records[3] = banksDetailsVi.buyNonCash?.let { java.lang.Double.toString(it) }
//            records[4] = banksDetailsVi.sellNonCash?.let { java.lang.Double.toString(it) }
            records[1] = banksDetailsVi.buyCash?.let {precision.format(it)}
            records[2] = banksDetailsVi.sellCash?.let { precision.format(it) }
//            records[3] = banksDetailsVi.buyNonCash?.let { precision.format(it)}
//            records[4] = banksDetailsVi.sellNonCash?.let { precision.format(it) }
            list.add(records)
        }



        val tableView =
            holder.itemView.findViewById<View>(R.id.tableView) as TableView<Array<String>>

        createTable(tableView,holder.itemView.context,list)
    }


    fun createTable(tableView: TableView<*>?, context: Context, tableData:MutableList<Array<String?>>) {


        tableView!!.columnCount = 3




        var rowColor = ContextCompat.getColor(context, R.color.greyColor)

        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.similarRowColor(rowColor));

//        tableView!!.setHeaderBackgroundColor(Color.parseColor("#2ecc71"))
        tableView!!.setHeaderBackgroundColor(ContextCompat.getColor(context,R.color.labelTextColor))



//        val Columns = arrayOf("Val","buy Csh","sell Csh","buy nonCsh","sell nonCsh")
        val Columns = arrayOf(context.getString(R.string.valute_col_label),context.getString(R.string.buycash_col_label),context.getString(R.string.sellcash_col_label))


        tableView!!.headerAdapter = SimpleTableHeaderAdapter(context, *Columns)
        val tableDataAdapter: TableDataAdapter<*> = SimpleTableDataAdapter(context, tableData)
        tableView!!.dataAdapter = tableDataAdapter

        var height = tableData.size * 60;

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