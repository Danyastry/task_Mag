package com.example.task_magnise.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PeriodicityButton(
    label: String,
    selectedPeriodicity: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = selectedPeriodicity != label,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(label)
    }
}