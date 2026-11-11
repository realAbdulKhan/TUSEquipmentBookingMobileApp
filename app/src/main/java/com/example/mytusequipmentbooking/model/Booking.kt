package com.example.mytusequipmentbooking.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookings",
    foreignKeys = [
        ForeignKey(
            entity = Equipment::class,
            parentColumns = ["id"],
            childColumns = ["equipmentId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index("equipmentId")
    ]
)
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val equipmentId: Int,
    val durationDays: Int,
    val purpose: String,
    val status: String
)