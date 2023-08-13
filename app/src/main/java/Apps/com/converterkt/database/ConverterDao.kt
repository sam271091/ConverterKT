package Apps.com.converterkt.database

import Apps.com.converterkt.pojo.*
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ConverterDao {

    @Query("SELECT * FROM valute")
    fun getAllValutes() : Flow<List<Valute>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertValutes(valutes:List<Valute>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertValute(valute:Valute?)


    @Query( "SELECT valuteInfo.date,valuteInfo.valute,valuteInfo.id,valuteInfo.nominal,valuteInfo.value FROM valuteInfo   LEFT JOIN favoriteValutes ON valuteInfo.valute = favoriteValutes.valute where (valuteInfo.valute,date) IN (SELECT valute,max(date) FROM valuteInfo Where date <= :chosenDate GROUP by valuteInfo.valute) ORDER BY favoriteValutes.valute <> ''  DESC")
//    fun getAllValuteInfo(chosenDate:Date) : LiveData<List<ValuteInfo>>
    fun getAllValuteInfo(chosenDate:Date) : Flow<List<ValuteInfo>>


    @Query( "SELECT * FROM valuteInfo where (valute,date) IN (SELECT valute,max(date) FROM valuteInfo where date <= :chosenDate GROUP by valute) AND valute in (:filter)")
//    fun getAllValuteInfoFiltered(filter:List<Valute>,chosenDate:Date) : LiveData<List<ValuteInfo>>
    fun getAllValuteInfoFiltered(filter:List<Valute>,chosenDate:Date) : Flow<List<ValuteInfo>>


    @Query( "SELECT * FROM valuteInfo where (valute,date) IN (SELECT valute,max(date) FROM valuteInfo where date <= :chosenDate GROUP by valute) AND valute in (:filter)")
    fun getAllValuteInfoFilteredNonObservable(filter:List<Valute>,chosenDate:Date) : List<ValuteInfo>

    @Query("SELECT * FROM valute where code LIKE :filter OR name LIKE :filter")
//    fun getFilteredValutes(filter:String) : LiveData<List<Valute>>
    fun getFilteredValutes(filter:String) : Flow<List<Valute>>

    @Query("SELECT * FROM valuteInfo where valute = :valute order by date DESC  Limit 5 ")
    fun getDataForTheGraph(valute:Valute) : List<ValuteInfo>


    @Query("SELECT * FROM valuteInfo where valute = :valute order by date ")
//    fun getDataCurrencyItem(valute:Valute) : LiveData<List<ValuteInfo>>
    fun getDataCurrencyItem(valute:Valute) : Flow<List<ValuteInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertValuteInfos(valuteInfo:List<ValuteInfo?>)

    @Query("SELECT * FROM valute where code =:code")
    fun getValuteByCode(code:String):Valute

    @Query( "SELECT * FROM valuteInfo where (valute,date) IN (SELECT valute,max(date) FROM valuteInfo WHERE valute = :valute AND date <= :chosenDate GROUP by valute)")
    fun getFilteredValuteInfo(valute:Valute,chosenDate:Date) : ValuteInfo




    //Banks

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBankInfos(bankInfo:List<BankInfo?>)

    @Query("DELETE FROM bankinfo")
    fun clearBankInfo()


    @Query("SELECT * FROM bankinfo")
//    fun getAllBankInfos() : LiveData<List<BankInfo>>
    fun getAllBankInfos() : Flow<List<BankInfo>>

//    @Query("SELECT 0 as id,0 as date, '' as currencyCode, bank,bankLogo,0 as buyCash,0 as buyNonCash,0 as buyForCards,0 as sellCash,0 as sellNonCash,0 as sellForCards  FROM BankInfo ORDER BY bank,bankLogo")
@Query("SELECT bank,bankName,bankLogo FROM BankInfo GROUP BY bank,bankName,bankLogo")
//    fun getAllBanksData() : LiveData<List<BankInfo>>
fun getAllBanksData() : Flow<List<BankInfo>>


    @Query("SELECT bank,bankName,buyCash,sellCash,bankLogo FROM BankInfo Where currencyCode = :valuteCode")
//    fun getBanksDataByValute(valuteCode:String) : LiveData<List<BankInfo>>
    fun getBanksDataByValute(valuteCode:String) : Flow<List<BankInfo>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteValute(favoriteValute:FavoriteValute)


    @Query("SELECT * FROM favoriteValutes")
    fun getFavouriteValutes(): List<FavoriteValute?>

    @Query("SELECT * FROM favoriteValutes WHERE code == :valuteCode")
    fun getFavouriteValuteById(valuteCode:String): FavoriteValute?

    @Delete
    fun deleteFavouriteValute(favoriteValute: FavoriteValute)


    @Query("SELECT valute FROM favoriteValutes")
//    fun getFavouriteValutesForFilter(): LiveData<List<Valute>>
    fun getFavouriteValutesForFilter(): Flow<List<Valute>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertbankBranch(bankBranch: BankBranch)


    @Query("DELETE FROM bankbranch WHERE bankCode == :bank")
    fun clearBranch(bank : String?)

    @Query("DELETE FROM bankbranch")
    fun clearBranches()

    @Query("SELECT * FROM bankbranch WHERE bankCode == :bank")
    fun getBranchesByBank(bank : String?) : Flow<List<BankBranch>>


}