package com.waitasecond.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.waitasecond.app.ui.HomeScreen
import com.waitasecond.app.ui.WaitASecondTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaitASecondTheme {
                HomeScreen()
            }
        }
    }
}
