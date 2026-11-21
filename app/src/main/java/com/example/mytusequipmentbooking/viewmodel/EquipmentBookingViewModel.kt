
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EquipmentBookingUiState(
    val equipment: List<Equipment> = emptyList(),
    val categories: List<Category> = emptyList(),
    val bookings: List<Booking> = emptyList(),
    val isLoading: Boolean = true,
    val loadError: String? = null,
    val actionMessage: String? = null,
    val isAdding: Boolean = false,
    val isDeleting: Boolean = false
)

class EquipmentBookingViewModel(application: Application) :
    AndroidViewModel(application) {

    private val database =
        EquipmentBookingDatabase.getDatabase(application)

    private val repository =
        EquipmentBookingRepository(database)

    private val _uiState =
        MutableStateFlow(EquipmentBookingUiState())

    val uiState: StateFlow<EquipmentBookingUiState> =
        _uiState.asStateFlow()

    // Stores the UID of the currently signed-in Firebase user.
    private var currentUserUid: String? = null

    // Keeps track of the active bookings observer.
    private var bookingsJob: Job? = null

    init {
        loadData()
    }

    // Loads the initial equipment and category data.
    private fun loadData() {

        viewModelScope.launch {
            try {
                repository.seedIfEmpty()

            } catch (cancelled: CancellationException) {
                throw cancelled

            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loadError = error.message
                            ?: "Unable to load app data."
                    )
                }
                return@launch
            }
        }

        // Observe equipment.
        viewModelScope.launch {
            try {
                repository.observeEquipment().collect { equipment ->
                    _uiState.update {
                        it.copy(
                            equipment = equipment,
                            isLoading = false
                        )
                    }
                }

            } catch (cancelled: CancellationException) {
                throw cancelled

            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loadError = error.message
                            ?: "Unable to load equipment."
                    )
                }
            }
        }

        // Observe categories.
        viewModelScope.launch {
            try {
                repository.observeCategories().collect { categories ->
                    _uiState.update {
                        it.copy(categories = categories)
                    }
                }

            } catch (cancelled: CancellationException) {
                throw cancelled

            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        loadError = error.message
                            ?: "Unable to load categories."
                    )
                }
            }
        }
    }

    // Updates the currently signed-in Firebase user.
    // Observes only bookings belonging to that user's UID.
    fun setUserUid(userUid: String?) {

        if (currentUserUid == userUid) return

        currentUserUid = userUid

        // Stop observing the previous user's bookings.
        bookingsJob?.cancel()
        bookingsJob = null

        // Clear previous bookings when the user changes or logs out.
        _uiState.update {
            it.copy(bookings = emptyList())
        }

        // Do not observe bookings if nobody is signed in.
        if (userUid == null) return

        // Observe bookings belonging to the signed-in user.
        bookingsJob = viewModelScope.launch {
            try {
                repository.observeBookings(userUid).collect { bookings ->
                    _uiState.update {
                        it.copy(bookings = bookings)
                    }
                }

            } catch (cancelled: CancellationException) {
                throw cancelled

            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        loadError = error.message
                            ?: "Unable to load bookings."
                    )
                }
            }
        }
    }

    // ADD EQUIPMENT
    fun addEquipment(equipment: Equipment) {

        val current = _uiState.value

        if (current.isLoading ||
            current.isAdding ||
            current.loadError != null
        ) return

        _uiState.update {
            it.copy(
                isAdding = true,
                actionMessage = null
            )
        }

        viewModelScope.launch {
            try {
                val id = repository.saveEquipment(equipment)

                _uiState.update {
                    it.copy(
                        actionMessage =
                            "Equipment saved successfully. ID: $id"
                    )
                }

            } catch (cancelled: CancellationException) {
                throw cancelled

            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        actionMessage = error.message
                            ?: "Unable to save equipment."
                    )
                }

            } finally {
                _uiState.update {
                    it.copy(isAdding = false)
                }
            }
        }
    }

    // ADD BOOKING
    fun addBooking(booking: Booking) {

        // A booking can only be created by a signed-in user.
        val userUid = currentUserUid

        if (userUid == null) {
            _uiState.update {
                it.copy(
                    actionMessage = "Please log in before making a booking."
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                // Attach the currently signed-in user's Firebase UID.
                val userBooking = booking.copy(userUid = userUid)

                repository.insertBooking(userBooking)

                _uiState.update {
                    it.copy(
                        actionMessage = "Booking saved successfully."
                    )
                }

            } catch (cancelled: CancellationException) {
                throw cancelled

            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        actionMessage = error.message
                            ?: "Unable to save booking."
                    )
                }
            }
        }
    }

    // DELETE EQUIPMENT
    fun deleteEquipment(equipmentId: Int) {

        val current = _uiState.value

        if (current.isLoading ||
            current.isAdding ||
            current.isDeleting ||
            current.loadError != null
        ) return

        _uiState.update {
            it.copy(
                isDeleting = true,
                actionMessage = null
            )
        }

        viewModelScope.launch {
            try {
                repository.deleteEquipment(equipmentId)

                _uiState.update {
                    it.copy(
                        actionMessage = "Equipment deleted successfully."
                    )
                }

            } catch (cancelled: CancellationException) {
                throw cancelled

            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        actionMessage = error.message
                            ?: "Unable to delete equipment."
                    )
                }

            } finally {
                _uiState.update {
                    it.copy(isDeleting = false)
                }
            }
        }
    }

    // CANCEL BOOKING
    fun cancelBooking(bookingId: Int) {

        val userUid = currentUserUid

        if (userUid == null) {
            _uiState.update {
                it.copy(
                    actionMessage = "Please log in before cancelling a booking."
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                // Only updates a booking belonging to the current user.
                repository.updateBookingStatus(
                    bookingId = bookingId,
                    userUid = userUid,
                    status = "Cancelled"
                )

                _uiState.update {
                    it.copy(
                        actionMessage = "Booking cancelled successfully."
                    )
                }

            } catch (cancelled: CancellationException) {
                throw cancelled

            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        actionMessage = error.message
                            ?: "Unable to cancel booking."
                    )
                }
            }
        }
    }
}
