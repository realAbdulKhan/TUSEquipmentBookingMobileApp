
package com.example.mytusequipmentbooking.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "equipment",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class Equipment(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val categoryId: Int,
    val name: String,
    val description: String
)
