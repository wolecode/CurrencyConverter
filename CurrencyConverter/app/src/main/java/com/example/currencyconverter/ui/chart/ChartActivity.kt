package com.example.currencyconverter.ui.chart

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.ActivityChartBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

class ChartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val entryList = mutableListOf<Entry>(Entry(0f, 10.1f), Entry(1f, 10.5f),
            Entry(2f, 11f), Entry(3f, 10.8f), Entry(4f, 9.2f),
            Entry(5f, 10.6f), Entry(6f, 9.0f))

        val lineSet = LineDataSet(entryList,"NEW CHART").apply {
            lineWidth = 4f
        }

        val alpha = listOf("Year1", "Year2", "Year3", "Year4", "Year5", "Year6", "Year7")

        val valueFormatter = object :ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return alpha[value.toInt()]
            }

        }

        with(binding.historicalChart){
            setDrawGridBackground(false)
            minimumWidth = (resources.displayMetrics.densityDpi * 300) / 160
            data = LineData(lineSet)
            axisRight.isEnabled = false
            axisLeft.setDrawGridLines(false)
            axisLeft.axisLineWidth = 6f
            //axisLeft.setDrawZeroLine(false)
            //axisRight.textColor = R.color.my_color
            //axisLeft.textColor = R.color.my_color
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.valueFormatter = valueFormatter
            xAxis.granularity = 1f
            xAxis.axisLineWidth = 6f
            invalidate()
        }

    }
}