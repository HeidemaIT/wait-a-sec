package com.learnfromsilence.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.learnfromsilence.app.ui.components.PrimaryButton
import com.learnfromsilence.app.ui.components.SecondaryButton
import com.learnfromsilence.app.ui.components.SitButton
import com.learnfromsilence.app.ui.components.TimerRing
import com.learnfromsilence.app.ui.design.DesignTokens

@Composable
fun HomeScreen(
    session: SilenceSessionState
) {
    SilenceEndChimeEffect(session = session)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0f)
    ) {
        when {
            session.awaitingFeedback -> FeedbackPrompt(session = session)
            session.isIdle -> IdleHome(
                durationSeconds = session.targetDurationSeconds,
                onBeginSit = session::restart
            )
            else -> ActiveSit(session = session)
        }
    }
}

@Composable
private fun IdleHome(
    durationSeconds: Int,
    onBeginSit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = DesignTokens.Spacing.lg,
                vertical = DesignTokens.Spacing.xxl
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Learn from Silence",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(DesignTokens.Spacing.md))

        Text(
            text = "Practice sitting quietly, one short sit at a time.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(DesignTokens.Spacing.xl))

        SitButton(
            text = formatBeginSitLabel(durationSeconds),
            onClick = onBeginSit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ActiveSit(
    session: SilenceSessionState
) {
    val secondsRemaining = session.secondsRemaining
    val isRunning = session.isRunning
    val isDone = session.isDone
    val targetDuration = session.targetDurationSeconds
    val motion = MaterialTheme.motionScheme

    val title = "Sit for ${formatSitDurationLabel(targetDuration)}"
    val prompt = when {
        !isRunning -> "Paused. Resume when you are ready."
        else -> "Close your eyes. Stay with the silence."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = DesignTokens.Spacing.lg)
            .padding(bottom = DesignTokens.Spacing.xxl + DesignTokens.Spacing.md)
    ) {
        AnimatedContent(
            targetState = title to prompt,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = DesignTokens.Spacing.xxl)
                .fillMaxWidth(),
            transitionSpec = {
                fadeIn(animationSpec = motion.defaultEffectsSpec()) togetherWith
                    fadeOut(animationSpec = motion.defaultEffectsSpec())
            },
            label = "sitCopy"
        ) { (currentTitle, currentPrompt) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentTitle,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))
                Text(
                    text = currentPrompt,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.82f)
                )
            }
        }

        TimerRing(
            secondsRemaining = secondsRemaining,
            totalSeconds = targetDuration,
            isBreathing = isRunning && !isDone,
            isDone = isDone,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(DesignTokens.Components.primaryButtonMinHeight),
            horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.md)
        ) {
            PrimaryButton(
                onClick = session::pauseOrResume,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                icon = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                iconContentDescription = if (isRunning) "Pause" else "Resume"
            )
            SecondaryButton(
                onClick = session::restart,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                icon = Icons.Filled.RestartAlt,
                iconContentDescription = "Restart"
            )
        }
    }
}

@Composable
private fun FeedbackPrompt(
    session: SilenceSessionState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = DesignTokens.Spacing.lg,
                vertical = DesignTokens.Spacing.xl
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "How was that length?",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(DesignTokens.Spacing.sm))

        Text(
            text = "Was ${formatSitDurationLabel(session.targetDurationSeconds)} too short, enough, or too long?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.82f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(DesignTokens.Spacing.xl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DesignTokens.Spacing.sm)
        ) {
            FeedbackChoiceButton(
                emoji = "😕",
                label = "Too short",
                onClick = { session.applyFeedback(SitLengthFeedback.TooShort) },
                modifier = Modifier.weight(1f)
            )
            FeedbackChoiceButton(
                emoji = "🙂",
                label = "Enough",
                onClick = { session.applyFeedback(SitLengthFeedback.Enough) },
                modifier = Modifier.weight(1f)
            )
            FeedbackChoiceButton(
                emoji = "😣",
                label = "Too long",
                onClick = { session.applyFeedback(SitLengthFeedback.TooLong) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FeedbackChoiceButton(
    emoji: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(DesignTokens.Components.sitButtonMinHeight + DesignTokens.Spacing.lg),
        shape = RoundedCornerShape(DesignTokens.Radius.lg)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(DesignTokens.Spacing.xs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
