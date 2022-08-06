package Apps.com.converterkt.fragments

import Apps.com.converterkt.ConverterViewModel
import Apps.com.converterkt.R
import Apps.com.converterkt.pojo.BankInfo
import Apps.com.converterkt.pojo.Valute
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.codecrafters.tableview.TableDataAdapter
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.listeners.TableDataClickListener
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders
import kotlinx.android.synthetic.main.bank_row_details.*
import kotlinx.android.synthetic.main.bank_row_details.textViewBankNameDetail
import kotlinx.android.synthetic.main.bank_row_details.tvBuyCashValue
import kotlinx.android.synthetic.main.bank_row_details.tvSellCashValue
import kotlinx.android.synthetic.main.bank_row_details.view.*
import kotlinx.android.synthetic.main.fragment_current_valute_banks_data.*
import kotlinx.android.synthetic.main.valute_history_fragment.*
import java.text.DecimalFormat
import java.util.*


class current_valute_banks_data(val currency: Valute, var viewModel : ConverterViewModel) : Fragment() {

    private var precision =  DecimalFormat("#,##0.0000")

    private var banksDataDetails = listOf<BankInfo>()

    private lateinit var tableView : TableView<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_valute_banks_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tableView = tableViewBanksByValute as TableView<Array<String>>

        viewModel.banksDataByValute(currency.code).observe(viewLifecycleOwner, Observer {
            createTableData(it,view)
            banksDataDetails = it
        })


        showBankDetails()

        super.onViewCreated(view, savedInstanceState)
    }


    fun showBankDetails(){
        var bottomSheetDialog = BottomSheetDialog(
            tableView.getContext(), R.style.BottomSheetDialogTheme
        )

        tableView.addDataClickListener(TableDataClickListener { rowIndex, clickedData ->
            var rowBankInfo = banksDataDetails.get(rowIndex)


            if (!bottomSheetDialog.isShowing){

                val bottomSheetView = LayoutInflater.from(tableView.context)
                    .inflate(
                        R.layout.bank_row_details,
                        bottomSheetContainerBankDetails
                    )


                bottomSheetView.textViewBankNameDetail.text = rowBankInfo.bankName.toString()
                bottomSheetView.tvBuyCashValue.text         = rowBankInfo.buyCash?.let {precision.format(it)}
                bottomSheetView.tvSellCashValue.text         = rowBankInfo.sellCash?.let {precision.format(it)}




                bottomSheetDialog.setContentView(bottomSheetView)



                bottomSheetDialog.show()

            }


        })


    }


    fun createTableData(banksDetails:List<BankInfo>,view: View){
        val list: MutableList<Array<String?>> = ArrayList()

        val valutelist: MutableList<String?> = ArrayList<String?>()




        for (banksDetailsVi in banksDetails) {
            val records = arrayOfNulls<String>(3)
            if (banksDetailsVi.bank == null) {
                records[0] = "None"
            } else {
                records[0] = banksDetailsVi.bankName
            }
            valutelist.add(banksDetailsVi.bank)
            records[1] = banksDetailsVi.buyCash?.let {precision.format(it)}
            records[2] = banksDetailsVi.sellCash?.let { precision.format(it) }

            list.add(records)
        }






        createTable(tableView,view.context,list)
    }


    fun createTable(tableView: TableView<*>?, context: Context, tableData:MutableList<Array<String?>>) {


        tableView!!.columnCount = 3




        var rowColor = ContextCompat.getColor(context, R.color.greyColor)

        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.similarRowColor(rowColor));

//        tableView!!.setHeaderBackgroundColor(Color.parseColor("#2ecc71"))
        tableView!!.setHeaderBackgroundColor(ContextCompat.getColor(context,R.color.labelTextColor))



//        val Columns = arrayOf("Val","buy Csh","sell Csh","buy nonCsh","sell nonCsh")
        val Columns = arrayOf(getString(R.string.bank_row_label),getString(R.string.buycash_col_label),getString(R.string.sellcash_col_label))


        tableView!!.headerAdapter = SimpleTableHeaderAdapter(context, *Columns)
        val tableDataAdapter: TableDataAdapter<*> = SimpleTableDataAdapter(context, tableData)
        tableView!!.dataAdapter = tableDataAdapter

        var height = tableData.size * 60;

//        tableView.layoutParams.height= context?.resources?.displayMetrics?.let {
//            dpToPx(height,
//                it?.density)
//        }!!

    }

    //    companion object {
//        @JvmStatic
//        fun newInstance() = current_valute_banks_data()
//            }
    }
