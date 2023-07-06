package Apps.com.converterkt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import Apps.com.converterkt.converters.DateConverter
import Apps.com.converterkt.converters.ValuteConverter
import Apps.com.converterkt.pojo.*
import Apps.com.converterkt.utils.MIGRATION_1_2
import Apps.com.converterkt.utils.MIGRATION_2_3
import Apps.com.converterkt.utils.MIGRATION_3_4

@Database(entities = [(Valute::class),(ValuteInfo::class),(BankInfo::class),(FavoriteValute::class),(BankBranch::class)], version = 4, exportSchema = true)
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
                DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4)
                .build()

            db = instance

            return instance
        }

    }




    abstract fun converterDao():ConverterDao
}