
package com.example.mytusequipmentbooking.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mytusequipmentbooking.model.Booking
import com.example.mytusequipmentbooking.model.Category
import com.example.mytusequipmentbooking.model.Equipment
import kotlinx.coroutines.flow.Flow


@Dao
interface EquipmentBookingDao {

    // Observe all categories
    @Query("SELECT * FROM categories")
    fun observeCategories(): Flow<List<Category>>

    // Observe all equipment
    @Query("SELECT * FROM equipment")
    fun observeEquipment(): Flow<List<Equipment>>

    // Observe all bookings
    @Query("SELECT * FROM bookings")
    fun observeBookings(): Flow<List<Booking>>

    // Count categories
    @Query("SELECT COUNT(*) FROM categories")
    suspend fun categoryCount(): Int

    // Count equipment
    @Query("SELECT COUNT(*) FROM equipment")
    suspend fun equipmentCount(): Int

    // Insert categories
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    // Insert equipment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEquipment(equipment: List<Equipment>)

    @Insert
    suspend fun insertNewEquipment(equipment: Equipment): Long

    // Delete equipment by id (we'll add this later)
    @Query("DELETE FROM equipment WHERE id = :equipmentId")
    suspend fun deleteEquipment(equipmentId: Int)
    // Insert a booking
    @Insert
    suspend fun insertBooking(booking: Booking): Long

    // Update a booking's status
    @Query("UPDATE bookings SET status = :status WHERE id = :bookingId")
    suspend fun updateBookingStatus(bookingId: Int, status: String)
}
