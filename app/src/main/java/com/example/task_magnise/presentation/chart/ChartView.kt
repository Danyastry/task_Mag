package com.example.task_magnise.presentation.chart

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.task_magnise.data.model.bars.Bar
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun ChartView(listData: List<Bar>) {
    if (listData.isEmpty()) return

    val entries = listData.mapIndexed { index, bar ->
        Entry(index.toFloat(), bar.close.toFloat())
    }

    AndroidView(modifier = Modifier
        .fillMaxHeight(0.5f)
        .fillMaxWidth(), factory = { context ->
        LineChart(context).apply {
            val dataSet = LineDataSet(entries, "Price").apply {
                color = context.getColor(android.R.color.holo_blue_dark)
                valueTextColor = context.getColor(android.R.color.black)
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
            }

            data = LineData(dataSet)
            description.isEnabled = false
            legend.isEnabled = true

            setTouchEnabled(true)
            setPinchZoom(true)
            setScaleEnabled(true)
            isDragEnabled = true

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
            }

            axisLeft.apply {
                setDrawGridLines(false)
                setDrawLabels(true)
            }

            axisRight.isEnabled = false

            animateX(1000)
            invalidate()
        }
    })
}