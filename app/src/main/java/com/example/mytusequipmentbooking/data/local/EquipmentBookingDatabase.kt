
package com.example.mytusequipmentbooking.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mytusequipmentbooking.model.Booking
import com.example.mytusequipmentbooking.model.Category
import com.example.mytusequipmentbooking.model.Equipment

@Database(
    entities = [
        Category::class,
        Equipment::class,
        Booking::class
    ],
    version = 2,
    exportSchema = true
)
abstract class EquipmentBookingDatabase : RoomDatabase() {

    abstract fun equipmentBookingDao(): EquipmentBookingDao

    companion object {

        // Migration from database version 1 to version 2.
        // Existing bookings receive an empty UID.
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE bookings ADD COLUMN userUid TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        @Volatile
        private var INSTANCE: EquipmentBookingDatabase? = null

        fun getDatabase(context: Context): EquipmentBookingDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EquipmentBookingDatabase::class.java,
                    "equipment_booking_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
