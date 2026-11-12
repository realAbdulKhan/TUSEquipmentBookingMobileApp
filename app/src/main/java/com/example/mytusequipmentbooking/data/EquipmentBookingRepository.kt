package com.example.mytusequipmentbooking.data

import androidx.room.withTransaction
import com.example.mytusequipmentbooking.data.local.EquipmentBookingDatabase
import com.example.mytusequipmentbooking.model.Booking
import com.example.mytusequipmentbooking.model.Equipment
import kotlinx.coroutines.flow.Flow

class EquipmentBookingRepository(
    private val database: EquipmentBookingDatabase
) {
    private val dao = database.equipmentBookingDao()

    fun observeCategories() =
        dao.observeCategories()

    fun observeEquipment() =
        dao.observeEquipment()

    val bookings: Flow<List<Booking>> = dao.observeBookings()

    suspend fun seedIfEmpty() {
        database.withTransaction {
            val categoryCount = dao.categoryCount()
            val equipmentCount = dao.equipmentCount()

            if (categoryCount == 0 && equipmentCount == 0) {
                dao.insertCategories(SampleData.categories)
                dao.insertEquipment(SampleData.equipment)
            }
        }
    }

    // SAVE EQUIPMENT
    suspend fun saveEquipment(equipment: Equipment): Long {
        return dao.insertNewEquipment(equipment)
    }

    // DELETE EQUIPMENT
    suspend fun deleteEquipment(equipmentId: Int) {
        dao.deleteEquipment(equipmentId)
    }

    // INSERT BOOKING
    suspend fun insertBooking(booking: Booking): Long {
        return dao.insertBooking(booking)
    }

    // UPDATE BOOKING STATUS
    suspend fun updateBookingStatus(bookingId: Int, status: String) {
        dao.updateBookingStatus(bookingId, status)
    }
}