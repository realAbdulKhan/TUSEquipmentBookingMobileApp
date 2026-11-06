package com.example.mytusequipmentbooking.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mytusequipmentbooking.model.Booking
import com.example.mytusequipmentbooking.model.Category
import com.example.mytusequipmentbooking.model.Equipment

@Database(
    entities = [
        Category::class,
        Equipment::class,
        Booking::class
    ],
    version = 1,
    exportSchema = true
)
abstract class EquipmentBookingDatabase : RoomDatabase() {

    abstract fun equipmentBookingDao(): EquipmentBookingDao

    companion object {
        @Volatile
        private var INSTANCE: EquipmentBookingDatabase? = null

        fun getDatabase(context: Context): EquipmentBookingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EquipmentBookingDatabase::class.java,
                    "equipment_booking_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}