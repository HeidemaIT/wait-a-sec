package com.waitasecond.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.waitasecond.app.ui.components.CalmAtmosphere
import com.waitasecond.app.ui.design.DesignTokens

@Composable
fun HomeScreen() {
    CalmAtmosphere {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = DesignTokens.Spacing.lg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "This is the Wait a second app",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}
