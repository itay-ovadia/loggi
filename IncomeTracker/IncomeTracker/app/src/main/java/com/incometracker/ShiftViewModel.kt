package com.incometracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Calendar

class ShiftViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.get(app).shiftDao()

    // Currently selected month (Calendar set to 1st of month, midnight)
    private val _selectedMonth = MutableLiveData<Pair<Long, Long>>()

    init {
        setCurrentMonth()
    }

    private fun setCurrentMonth() {
        val (start, end) = monthRange(Calendar.getInstance())
        _selectedMonth.value = Pair(start, end)
    }

    fun setMonth(cal: Calendar) {
        val (start, end) = monthRange(cal)
        _selectedMonth.value = Pair(start, end)
    }

    private fun monthRange(cal: Calendar): Pair<Long, Long> {
        val start = Calendar.getInstance().apply {
            set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        val end = Calendar.getInstance().apply {
            set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.MONTH, 1)
            add(Calendar.MILLISECOND, -1)
        }.timeInMillis
        return Pair(start, end)
    }

    val shifts: LiveData<List<Shift>> = _selectedMonth.switchMap { (start, end) ->
        dao.getShiftsForMonth(start, end)
    }

    val selectedMonthRange: LiveData<Pair<Long, Long>> = _selectedMonth

    fun addShift(shift: Shift) = viewModelScope.launch { dao.insert(shift) }
    fun deleteShift(shift: Shift) = viewModelScope.launch { dao.delete(shift) }
}
