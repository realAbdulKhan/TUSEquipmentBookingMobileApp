package com.example.mytusequipmentbooking.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")

// Data class representing one category in the application.
// Room uses this class to create and manage records in the categories table.
data class Category(

    // Primary key that uniquely identifies each category.
    // Unlike the Booking and Equipment entities, this ID is not auto-generated.
    // The category ID must be supplied when creating a Category object.
    @PrimaryKey
    val id: Int,

    // Stores the name of the category.
    // Examples could include Cameras, Laptops or Audio Equipment.
    // This name is displayed when users browse or add equipment.
    val name: String
)