package demo.com.converterkt.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import demo.com.converterkt.pojo.Valute

@Dao
interface ConverterDao {

    @Query("SELECT * FROM valute")
    fun getAllValutes() : LiveData<List<Valute>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertValutes(valutes:List<Valute>?)
}