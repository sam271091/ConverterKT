package Apps.com.converterkt.utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // https://developer.android.com/reference/android/arch/persistence/room/ColumnInfo
        /*
        database.execSQL("ALTER TABLE pin "
                + " ADD COLUMN is_location_accurate INTEGER NOT NULL DEFAULT 0")
        database.execSQL("UPDATE pin "
                + " SET is_location_accurate = 0 WHERE lat IS NULL")
        database.execSQL("UPDATE pin "
                + " SET is_location_accurate = 1 WHERE lat IS NOT NULL")
                */
//        database.execSQL("CREATE TABLE IF NOT EXISTS `BankInfo` (`id` INTEGER NOT NULL, `date` INTEGER NOT NULL, `bank` TEXT NOT NULL, `bankLogo` TEXT NOT NULL, `currencyCode` TEXT NOT NULL, `buyCash` REAL NOT NULL, `buyNonCash` REAL NOT NULL, `buyForCards` REAL NOT NULL, `sellCash` REAL NOT NULL, `sellNonCash` REAL NOT NULL, `sellForCards` REAL NOT NULL, PRIMARY KEY(`id`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `BankInfo` (`id` INTEGER , `date` INTEGER, `bank` TEXT, `bankLogo` TEXT, `currencyCode` TEXT, `buyCash` REAL, `buyNonCash` REAL, `buyForCards` REAL, `sellCash` REAL, `sellNonCash` REAL, `sellForCards` REAL, PRIMARY KEY(`id`))")
    }
}