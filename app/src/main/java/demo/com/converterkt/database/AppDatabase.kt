package demo.com.converterkt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import demo.com.converterkt.converters.DateConverter
import demo.com.converterkt.converters.UUIDConverter
import demo.com.converterkt.converters.ValuteConverter
import demo.com.converterkt.pojo.Valute
import demo.com.converterkt.pojo.ValuteInfo

@Database(entities = [Valute::class,ValuteInfo::class], version = 1, exportSchema = false)
@TypeConverters(value = [ValuteConverter::class,DateConverter::class])
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var db : AppDatabase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()
        fun getInstance(context: Context):AppDatabase{
            synchronized(LOCK){
                db?.let { return it }
            }

            val instance = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DB_NAME).build()

            db = instance

            return instance
        }

    }

    abstract fun converterDao():ConverterDao
}