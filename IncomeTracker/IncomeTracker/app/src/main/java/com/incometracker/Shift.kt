package com.incometracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class Shift(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workplaceId: Int,       // 0 = Workplace A (variable), 1 = Workplace B (fixed)
    val date: Long,             // epoch millis
    val hours: Double,
    val tariff: Int,            // 40, 60, or 80 for A; always 40 for B
    val note: String = ""
) {
    val earnings: Double get() = hours * tariff
}
