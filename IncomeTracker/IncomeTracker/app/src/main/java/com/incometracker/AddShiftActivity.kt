package com.incometracker

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddShiftActivity : AppCompatActivity() {

    private var selectedDateMs = System.currentTimeMillis()
    private val dateFmt = SimpleDateFormat("EEE, d MMMM yyyy", Locale.getDefault())
    private var selectedTariff = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shift)

        supportActionBar?.title = "Add shift"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tvDate = findViewById<TextView>(R.id.tvDate)
        val btnDate = findViewById<Button>(R.id.btnDate)
        val spinnerWorkplace = findViewById<Spinner>(R.id.spinnerWorkplace)
        val layoutTariff = findViewById<LinearLayout>(R.id.layoutTariff)
        val rgTariff = findViewById<RadioGroup>(R.id.rgTariff)
        val etHours = findViewById<EditText>(R.id.etHours)
        val etNote = findViewById<EditText>(R.id.etNote)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Date
        tvDate.text = dateFmt.format(selectedDateMs)
        btnDate.setOnClickListener {
            val cal = Calendar.getInstance().apply { timeInMillis = selectedDateMs }
            DatePickerDialog(this, { _, y, m, d ->
                Calendar.getInstance().apply {
                    set(y, m, d, 12, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                    selectedDateMs = timeInMillis
                }
                tvDate.text = dateFmt.format(selectedDateMs)
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Workplace spinner
        ArrayAdapter.createFromResource(
            this, R.array.workplaces, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerWorkplace.adapter = adapter
        }

        spinnerWorkplace.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, pos: Int, id: Long) {
                layoutTariff.visibility = if (pos == 0) LinearLayout.VISIBLE else LinearLayout.GONE
                if (pos == 1) selectedTariff = 40
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Tariff radio
        rgTariff.check(R.id.rb60)
        rgTariff.setOnCheckedChangeListener { _, checkedId ->
            selectedTariff = when (checkedId) {
                R.id.rb40 -> 40
                R.id.rb80 -> 80
                else -> 60
            }
        }

        // Save
        btnSave.setOnClickListener {
            val hoursText = etHours.text.toString()
            if (hoursText.isBlank()) {
                etHours.error = "Enter hours worked"
                return@setOnClickListener
            }
            val hours = hoursText.toDoubleOrNull()
            if (hours == null || hours <= 0) {
                etHours.error = "Enter a valid number"
                return@setOnClickListener
            }

            val workplaceId = spinnerWorkplace.selectedItemPosition

            setResult(RESULT_OK, Intent().apply {
                putExtra("workplaceId", workplaceId)
                putExtra("dateMs", selectedDateMs)
                putExtra("hours", hours)
                putExtra("tariff", if (workplaceId == 1) 40 else selectedTariff)
                putExtra("note", etNote.text.toString().trim())
            })
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
