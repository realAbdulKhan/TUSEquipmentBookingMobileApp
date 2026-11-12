
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

    suspend fun saveEquipment(equipment: Equipment): Long {
        return dao.insertNewEquipment(equipment)
    }

    suspend fun insertBooking(booking: Booking): Long {
        return dao.insertBooking(booking)
    }

    suspend fun updateBookingStatus(bookingId: Int, status: String) {
        dao.updateBookingStatus(bookingId, status)
    }
}
