
package com.example.mytusequipmentbooking.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytusequipmentbooking.data.EquipmentBookingRepository
import com.example.mytusequipmentbooking.data.local.EquipmentBookingDatabase
import com.example.mytusequipmentbooking.model.Booking
import com.example.mytusequipmentbooking.model.Category
import com.example.mytusequipmentbooking.model.Equipment
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class EquipmentBookingUiState(
    val categories: List<Category> = emptyList(),
    val equipment: List<Equipment> = emptyList(),
    val bookings: List<Booking> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class EquipmentBookingViewModel(application: Application) :
    AndroidViewModel(application) {

    private val database =
        EquipmentBookingDatabase.getDatabase(application)

    private val repository =
        EquipmentBookingRepository(database)

    private val _uiState = MutableStateFlow(EquipmentBookingUiState())
    val uiState: StateFlow<EquipmentBookingUiState> = _uiState.asStateFlow()

    init {
        seedDatabase()
        observeData()
    }

    private fun seedDatabase() {
        viewModelScope.launch {
            try {
                repository.seedIfEmpty()
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unable to load sample data."
                )
            }
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                repository.categories,
                repository.equipment,
                repository.bookings
            ) { categories, equipment, bookings ->
                EquipmentBookingUiState(
                    categories = categories,
                    equipment = equipment,
                    bookings = bookings,
                    isLoading = false
                )
            }.catch { e ->
                if (e is CancellationException) throw e

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unable to load app data."
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun addBooking(booking: Booking) {
        viewModelScope.launch {
            try {
                repository.insertBooking(booking)
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unable to save booking."
                )
            }
        }
    }

    fun cancelBooking(bookingId: Int) {
        viewModelScope.launch {
            try {
                repository.updateBookingStatus(
                    bookingId = bookingId,
                    status = "Cancelled"
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unable to cancel booking."
                )
            }
        }
    }
}
