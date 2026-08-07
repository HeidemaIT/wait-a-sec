package com.waitasecond.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

/**
 * Soft vertical wash so screens feel calm rather than flat scaffolding.
 */
@Composable
fun CalmAtmosphere(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val background = MaterialTheme.colorScheme.background
    val wash = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.42f)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(background, wash, background)
                )
            )
    ) {
        content()
    }
}
