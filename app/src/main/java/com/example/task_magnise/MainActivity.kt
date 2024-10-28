package com.example.task_magnise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.task_magnise.presentation.main.MainScreen
import com.example.task_magnise.ui.theme.Task_MagniseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task_MagniseTheme {
               MainScreen()
            }
        }
    }
}
