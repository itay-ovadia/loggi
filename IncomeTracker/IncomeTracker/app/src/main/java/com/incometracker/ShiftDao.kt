package com.incometracker

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShiftDao {

    @Query("SELECT * FROM shifts ORDER BY date DESC")
    fun getAllShifts(): LiveData<List<Shift>>

    @Query("SELECT * FROM shifts WHERE date >= :startMs AND date <= :endMs ORDER BY date DESC")
    fun getShiftsForMonth(startMs: Long, endMs: Long): LiveData<List<Shift>>

    @Insert
    suspend fun insert(shift: Shift)

    @Delete
    suspend fun delete(shift: Shift)

    @Update
    suspend fun update(shift: Shift)
}
