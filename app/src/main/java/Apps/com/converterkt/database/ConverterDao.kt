package Apps.com.converterkt.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import Apps.com.converterkt.pojo.Valute
import Apps.com.converterkt.pojo.ValuteInfo

@Dao
interface ConverterDao {

    @Query("SELECT * FROM valute")
    fun getAllValutes() : LiveData<List<Valute>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertValutes(valutes:List<Valute>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertValute(valute:Valute?)


    @Query( "SELECT * FROM valuteInfo where (valute,date) IN (SELECT valute,max(date) FROM valuteInfo GROUP by valute)")
    fun getAllValuteInfo() : LiveData<List<ValuteInfo>>


    @Query( "SELECT * FROM valuteInfo where (valute,date) IN (SELECT valute,max(date) FROM valuteInfo GROUP by valute) AND valute in (:filter)")
    fun getAllValuteInfoFiltered(filter:List<Valute>) : LiveData<List<ValuteInfo>>

    @Query("SELECT * FROM valute where code LIKE :filter OR name LIKE :filter")
    fun getFilteredValutes(filter:String) : LiveData<List<Valute>>

    @Query("SELECT * FROM valuteInfo where valute = :valute order by date ASC Limit 3 ")
    fun getDataForTheGraph(valute:Valute) : LiveData<List<ValuteInfo>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertValuteInfos(valuteInfo:List<ValuteInfo?>)

}