package com.incometracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var vm: ShiftViewModel
    private lateinit var adapter: ShiftAdapter

    private lateinit var tvMonth: TextView
    private lateinit var tvTotalA: TextView
    private lateinit var tvTotalB: TextView
    private lateinit var tvGrandTotal: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var recycler: RecyclerView

    private val monthFmt = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private val currentCal = Calendar.getInstance()

    private val addShiftLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val workplaceId = data.getIntExtra("workplaceId", 0)
            val dateMs = data.getLongExtra("dateMs", System.currentTimeMillis())
            val hours = data.getDoubleExtra("hours", 0.0)
            val tariff = data.getIntExtra("tariff", 40)
            val note = data.getStringExtra("note") ?: ""
            vm.addShift(Shift(
                workplaceId = workplaceId,
                date = dateMs,
                hours = hours,
                tariff = tariff,
                note = note
            ))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMonth = findViewById(R.id.tvMonth)
        tvTotalA = findViewById(R.id.tvTotalA)
        tvTotalB = findViewById(R.id.tvTotalB)
        tvGrandTotal = findViewById(R.id.tvGrandTotal)
        tvEmpty = findViewById(R.id.tvEmpty)
        recycler = findViewById(R.id.recycler)

        vm = ViewModelProvider(this)[ShiftViewModel::class.java]

        adapter = ShiftAdapter { shift ->
            AlertDialog.Builder(this)
                .setTitle("Delete shift?")
                .setMessage("Remove this shift from ${java.text.SimpleDateFormat("d MMM", Locale.getDefault()).format(java.util.Date(shift.date))}?")
                .setPositiveButton("Delete") { _, _ -> vm.deleteShift(shift) }
                .setNegativeButton("Cancel", null)
                .show()
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        vm.shifts.observe(this) { shifts ->
            adapter.submitList(shifts)
            tvEmpty.visibility = if (shifts.isEmpty()) View.VISIBLE else View.GONE

            val totalA = shifts.filter { it.workplaceId == 0 }.sumOf { it.earnings }
            val totalB = shifts.filter { it.workplaceId == 1 }.sumOf { it.earnings }
            tvTotalA.text = "₪${String.format("%.0f", totalA)}"
            tvTotalB.text = "₪${String.format("%.0f", totalB)}"
            tvGrandTotal.text = "₪${String.format("%.0f", totalA + totalB)}"
        }

        updateMonthLabel()

        findViewById<View>(R.id.btnPrevMonth).setOnClickListener {
            currentCal.add(Calendar.MONTH, -1)
            updateMonthLabel()
            vm.setMonth(currentCal)
        }

        findViewById<View>(R.id.btnNextMonth).setOnClickListener {
            currentCal.add(Calendar.MONTH, 1)
            updateMonthLabel()
            vm.setMonth(currentCal)
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            addShiftLauncher.launch(Intent(this, AddShiftActivity::class.java))
        }
    }

    private fun updateMonthLabel() {
        tvMonth.text = monthFmt.format(currentCal.time)
    }
}
