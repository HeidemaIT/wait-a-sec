package com.learnfromsilence.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.learnfromsilence.app.ui.LearnFromSilenceApp
import com.learnfromsilence.app.ui.LearnFromSilenceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnFromSilenceTheme {
                LearnFromSilenceApp()
            }
        }
    }
}
