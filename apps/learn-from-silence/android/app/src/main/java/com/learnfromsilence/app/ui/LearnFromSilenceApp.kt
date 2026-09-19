package com.learnfromsilence.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.learnfromsilence.app.ui.components.CalmAtmosphere

@Composable
fun LearnFromSilenceApp() {
    val session = rememberSilenceSession()

    CalmAtmosphere(modifier = Modifier.fillMaxSize()) {
        HomeScreen(session = session)
    }
}
