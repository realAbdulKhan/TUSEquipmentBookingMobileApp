
package com.example.mytusequipmentbooking.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    // Tells Room to create a database table called "equipment"
    // using this data class.
    tableName = "equipment",

    // Defines the relationship between the equipment table and categories table.
    // Each equipment record must belong to an existing category.
    foreignKeys = [
        ForeignKey(
            // Category is the parent table that equipment references.
            entity = Category::class,

            // References the primary key column in the categories table.
            parentColumns = ["id"],

            // References the categoryId column in the equipment table.
            // This connects each equipment record to its category.
            childColumns = ["categoryId"],

            // Prevents a category from being deleted while equipment
            // records still reference that category.
            onDelete = ForeignKey.RESTRICT
        )
    ],

    // Creates an index on the categoryId column.
    // This helps the database find equipment belonging to a particular category.
    indices = [Index(value = ["categoryId"])]
)

// Data class representing one equipment record in the equipment table.
// Each object stores the details of an item available for booking.
data class Equipment(

    // Primary key that uniquely identifies each equipment record.
    // Room automatically generates the ID when a new record is inserted.
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    // Stores the ID of the category this equipment belongs to.
    // It must match an existing category ID because of the foreign key relationship.
    // Used to display the correct category name alongside equipment.
    val categoryId: Int,

    // Stores the name of the equipment.
    // This is displayed in the equipment catalogue and details screen.
    val name: String,

    // Stores a description of the equipment.
    // This is displayed to give users more information before booking.
    val description: String
)