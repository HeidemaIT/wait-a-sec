package com.learnfromsilence.app.ui.design

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Design tokens adapted from ninety-seconds-app (shared/design/ninety-seconds/tokens.json).
 */
object DesignTokens {
    object Light {
        val primary = Color(0xFF2F5D62)
        val onPrimary = Color(0xFFFFFFFF)
        val secondary = Color(0xFFDFE7E8)
        val onSecondary = Color(0xFF1F3336)
        val background = Color(0xFFF7F9F9)
        val onBackground = Color(0xFF1A2426)
        val surface = Color(0xFFFFFFFF)
        val onSurface = Color(0xFF1A2426)
        val surfaceVariant = Color(0xFFE8EFEF)
        val onSurfaceVariant = Color(0xFF3E5154)
        val outline = Color(0xFFB8C7C9)
    }

    object Dark {
        val primary = Color(0xFF8CB8BD)
        val onPrimary = Color(0xFF102023)
        val secondary = Color(0xFF31484C)
        val onSecondary = Color(0xFFDCE7E8)
        val background = Color(0xFF121819)
        val onBackground = Color(0xFFE4ECEC)
        val surface = Color(0xFF182326)
        val onSurface = Color(0xFFE4ECEC)
        val surfaceVariant = Color(0xFF243539)
        val onSurfaceVariant = Color(0xFFB8CACE)
        val outline = Color(0xFF4A6166)
    }

    object Semantic {
        val sit = Color(0xFF3D7A80)
        val sitOn = Color(0xFFFFFFFF)
        val success = Color(0xFF4F7A59)
    }

    object Spacing {
        val xs = 4.dp
        val sm = 8.dp
        val md = 16.dp
        val lg = 24.dp
        val xl = 32.dp
        val xxl = 48.dp
    }

    object Radius {
        val sm = 8.dp
        val md = 12.dp
        val lg = 16.dp
        val full = 999.dp
    }

    object Motion {
        val breathInMs = 4000L
        val breathOutMs = 6000L
        val timerTickMs = 1000L
        val screenTransitionMs = 300L
    }

    object Components {
        val primaryButtonMinHeight = 56.dp
        val primaryButtonHorizontalPadding = 24.dp
        val sitButtonMinHeight = 72.dp
        val sitButtonHorizontalPadding = 32.dp
        val timerRingSize = 240.dp
        val timerRingStrokeWidth = 8.dp
    }
}
