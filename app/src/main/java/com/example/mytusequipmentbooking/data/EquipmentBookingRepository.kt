package com.example.mytusequipmentbooking.data

import androidx.room.withTransaction
import com.example.mytusequipmentbooking.data.local.EquipmentBookingDatabase
import com.example.mytusequipmentbooking.model.Booking
import kotlinx.coroutines.flow.Flow

class EquipmentBookingRepository(
    private val database: EquipmentBookingDatabase
) {

    private val dao = database.equipmentBookingDao()

    val categories = dao.observeCategories()

    val equipment = dao.observeEquipment()

    fun observeBookingsForUser(userUid: String): Flow<List<Booking>> {
        return dao.observeBookingsForUser(userUid)
    }

    suspend fun insertBooking(booking: Booking): Long {
        return dao.insertBooking(booking)
    }

    suspend fun seedIfEmpty() {
        val categoryCount = dao.categoryCount()
        val equipmentCount = dao.equipmentCount()

        if (categoryCount == 0 && equipmentCount == 0) {
            database.withTransaction {
                dao.insertCategories(SampleData.categories)
                dao.insertEquipment(SampleData.equipment)
            }
        }
    }
}