package com.ezequielc.zekestockmarketnews.ui.chart

import android.content.Context
import android.widget.TextView
import com.ezequielc.zekestockmarketnews.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight

class CustomMarkView(
    context: Context,
    layoutResource: Int,
    private val candleEntryTime: List<String>?,
    private val candleEntryVolume: List<String>?,
) : MarkerView(context, layoutResource) {
    constructor(context: Context) : this(context, R.layout.custom_marker_view_layout, null, null)

    private val openEntryTextView = findViewById<TextView>(R.id.open_entry_textview)
    private val highEntryTextView = findViewById<TextView>(R.id.high_entry_textview)
    private val lowEntryTextView = findViewById<TextView>(R.id.low_entry_textview)
    private val closeEntryTextView = findViewById<TextView>(R.id.close_entry_textview)
    private val volumeEntryTextView = findViewById<TextView>(R.id.volume_entry_textview)
    private val timeEntryTextView = findViewById<TextView>(R.id.time_entry_textview)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)

        val candleEntry = e as CandleEntry
        openEntryTextView.text = getString(R.string.open_entry_text, candleEntry.open.toString())
        highEntryTextView.text = getString(R.string.high_entry_text, candleEntry.high.toString())
        lowEntryTextView.text = getString(R.string.low_entry_text, candleEntry.low.toString())
        closeEntryTextView.text = getString(R.string.close_entry_text, candleEntry.close.toString())
        volumeEntryTextView.text =
            getString(R.string.volume_entry_text, candleEntryVolume!![candleEntry.x.toInt()])
        timeEntryTextView.text = candleEntryTime!![candleEntry.x.toInt()]
    }

    private fun getString(int: Int, args: String) = context?.resources?.getString(int, args)
}