package com.incometracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ShiftAdapter(
    private val onDelete: (Shift) -> Unit
) : ListAdapter<Shift, ShiftAdapter.VH>(DIFF) {

    private val dateFmt = SimpleDateFormat("EEE, d MMM", Locale.getDefault())

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Shift>() {
            override fun areItemsTheSame(a: Shift, b: Shift) = a.id == b.id
            override fun areContentsTheSame(a: Shift, b: Shift) = a == b
        }

        val WORKPLACES = arrayOf("Workplace A", "Workplace B")
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvWorkplace: TextView = view.findViewById(R.id.tvWorkplace)
        val tvHours: TextView = view.findViewById(R.id.tvHours)
        val tvRate: TextView = view.findViewById(R.id.tvRate)
        val tvEarnings: TextView = view.findViewById(R.id.tvEarnings)
        val tvNote: TextView = view.findViewById(R.id.tvNote)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shift, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val shift = getItem(position)
        holder.tvDate.text = dateFmt.format(Date(shift.date))
        holder.tvWorkplace.text = WORKPLACES[shift.workplaceId]
        holder.tvHours.text = "${shift.hours}h"
        holder.tvRate.text = "₪${shift.tariff}/h"
        holder.tvEarnings.text = "₪${String.format("%.0f", shift.earnings)}"
        if (shift.note.isNotBlank()) {
            holder.tvNote.text = shift.note
            holder.tvNote.visibility = View.VISIBLE
        } else {
            holder.tvNote.visibility = View.GONE
        }
        holder.btnDelete.setOnClickListener { onDelete(shift) }
    }
}
