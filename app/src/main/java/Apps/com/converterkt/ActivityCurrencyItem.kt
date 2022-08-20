package Apps.com.converterkt

import Apps.com.converterkt.adapters.CurrencyItemDataAdapter
import Apps.com.converterkt.fragments.current_valute_banks_data
import Apps.com.converterkt.fragments.valute_history_fragment
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.utils.dpToPx
import Apps.com.converterkt.utils.getValuteFlagPath
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_currency_item.*
import kotlinx.android.synthetic.main.activity_valute_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityCurrencyItem : AppCompatActivity() {
    private lateinit var viewModel : ConverterViewModel
    val adapter = CurrencyItemDataAdapter(this)
    var isFavorite = false
    lateinit var valute:Valute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_item)

        viewModel = ViewModelProvider(this)[ConverterViewModel::class.java]




      val currency = intent.getSerializableExtra("currency")

        valute = (currency as Valute)

        checkFavorite()


        tabsChanger.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                var currentPos = tab?.position

                when(currentPos){
                    0->replaceFragment(valute_history_fragment(currency,viewModel,adapter))
                    1->replaceFragment(current_valute_banks_data(currency as Valute,viewModel))
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        replaceFragment(valute_history_fragment(currency,viewModel,adapter))



//        rvValuteInfo.adapter = adapter
//
        fillTheData(currency as Valute?)
//
//        rvValuteInfo.layoutManager = LinearLayoutManager(this)
//
//
//        floatingScrollUp.setOnClickListener {
//            rvValuteInfo.scrollToPosition(0)
//            floatingScrollUp.isVisible = false
//            floatingScrollDown.isVisible = true
//        }
//
//        floatingScrollDown.setOnClickListener {
//            rvValuteInfo.scrollToPosition(adapter.itemCount-1)
//            floatingScrollDown.isVisible = false
//            floatingScrollUp.isVisible = true
//
//        }
//
//
//        rvValuteInfo.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                when (newState){
//                    RecyclerView.SCROLL_STATE_IDLE -> {
//                        var linearLayoutManager =  recyclerView.layoutManager as LinearLayoutManager
//                        var currentItem = linearLayoutManager.findFirstVisibleItemPosition()
////                        Log.v("scroll_A", "currentItem $currentItem")
//                        floatingScrollUp.isVisible = currentItem != 0
//
//
//                        floatingScrollDown.isVisible = currentItem != (adapter.itemCount-1) - 4
//                    }
//                }
//
//            }

//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//            }
//        })

        imageViewAddToFavourite.setOnClickListener {
            onClickChangeFavourite()
        }




    }


    fun checkFavorite(){
        CoroutineScope(Dispatchers.IO).launch {
            isFavorite = viewModel.getFavoriteValuteById(valute) != null
            setFavourite()
        }
    }

    fun onClickChangeFavourite() {
        if (!isFavorite) {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.insertFavoriteValute(valute)
                checkFavorite()
            }
            Toast.makeText(this, getString(R.string.add_to_favourite), Toast.LENGTH_SHORT).show()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteFavoriteValute(valute)
                checkFavorite()
            }

            Toast.makeText(this, getString(R.string.remove_from_favourite), Toast.LENGTH_SHORT)
                .show()
        }



//        setFavourite()

    }

    fun setFavourite(){

        if (!isFavorite) {
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_add_to)
        } else {
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_remove)
        }
    }




    fun replaceFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment)
        fragmentTransaction.commit()
    }


    fun fillTheData(currency:Valute?){
        Picasso.get().load(getValuteFlagPath(currency)).into(flagIV)
        flagIV.layoutParams.width = dpToPx(58,resources.displayMetrics.density)
        flagIV.layoutParams.height = dpToPx(84,resources.displayMetrics.density)
        codeTV.text = currency?.code
        nameTV.text = currency?.name

//        if (currency != null){
//            fillTheList(currency)
//        }


    }

//    fun fillTheList(currency:Valute){
//        viewModel.getDataCurrencyItem(currency).observe(this, Observer {
//            adapter.valuteInfoList = it
//            rvValuteInfo.scrollToPosition(adapter.itemCount-1)
//            floatingScrollDown.isVisible = false
//        })
//    }

    companion object {
        fun newIntent(context:Context) : Intent{
            val intent = Intent(context,ActivityCurrencyItem::class.java)
            return intent
        }
    }
}