
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

    fun observeCategories() = dao.observeCategories()

    fun observeEquipment() = dao.observeEquipment()

    // Observes bookings belonging to a specific Firebase user.
    fun observeBookings(userUid: String): Flow<List<Booking>> {
        return dao.observeBookings(userUid)
    }

    // Inserts sample categories and equipment if the database is empty.
    suspend fun seedIfEmpty() {
        suspend fun seedIfEmpty() {
            database.withTransaction {
                if (dao.categoryCount() == 0 && dao.equipmentCount() == 0) {
                    dao.insertCategories(SampleData.categories)
                    dao.insertEquipment(SampleData.equipment)
                }
            }
        }
    }

    // Saves new equipment and returns its generated ID.
    suspend fun saveEquipment(equipment: Equipment): Long {
        return dao.insertNewEquipment(equipment)
    }

    // Deletes equipment using its ID.
    suspend fun deleteEquipment(equipmentId: Int) {
        dao.deleteEquipment(equipmentId)
    }

    // Inserts a new booking.
    suspend fun insertBooking(booking: Booking): Long {
        return dao.insertBooking(booking)
    }

    // Updates booking status only for the specified user.
    suspend fun updateBookingStatus(
        bookingId: Int,
        userUid: String,
        status: String
    ) {
        dao.updateBookingStatus(bookingId, userUid, status)
    }
}
