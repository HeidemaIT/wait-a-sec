package com.learnfromsilence.app.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.learnfromsilence.app.ui.design.DesignTokens

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    iconContentDescription: String? = null
) {
    val isIconOnly = icon != null && text == null
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minHeight = DesignTokens.Components.primaryButtonMinHeight
        ),
        enabled = enabled,
        shape = RoundedCornerShape(DesignTokens.Radius.lg),
        contentPadding = if (isIconOnly) {
            PaddingValues(horizontal = DesignTokens.Spacing.sm)
        } else {
            PaddingValues(
                horizontal = DesignTokens.Components.primaryButtonHorizontalPadding,
                vertical = DesignTokens.Spacing.sm
            )
        }
    ) {
        if (icon != null && text == null) {
            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription,
                modifier = Modifier.size(DesignTokens.Spacing.lg)
            )
        } else {
            Text(text = text.orEmpty(), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun SitButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minHeight = DesignTokens.Components.sitButtonMinHeight
        ),
        shape = RoundedCornerShape(DesignTokens.Radius.lg),
        colors = ButtonDefaults.buttonColors(
            containerColor = DesignTokens.Semantic.sit,
            contentColor = DesignTokens.Semantic.sitOn
        ),
        contentPadding = PaddingValues(
            horizontal = DesignTokens.Components.sitButtonHorizontalPadding,
            vertical = DesignTokens.Spacing.md
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    iconContentDescription: String? = null
) {
    val isIconOnly = icon != null && text == null
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(
            minHeight = DesignTokens.Components.primaryButtonMinHeight
        ),
        shape = RoundedCornerShape(DesignTokens.Radius.lg),
        contentPadding = if (isIconOnly) {
            PaddingValues(horizontal = DesignTokens.Spacing.sm)
        } else {
            ButtonDefaults.ContentPadding
        }
    ) {
        if (icon != null && text == null) {
            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription,
                modifier = Modifier.size(DesignTokens.Spacing.lg)
            )
        } else {
            Text(text = text.orEmpty(), style = MaterialTheme.typography.labelLarge)
        }
    }
}
