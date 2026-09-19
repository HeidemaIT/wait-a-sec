package com.learnfromsilence.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.learnfromsilence.app.ui.design.DesignTokens
import kotlin.math.abs
import kotlin.math.cos
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val BreathScaleMin = 0.92f
private const val BreathScaleMax = 1.08f
private const val AnnounceEverySeconds = 15

private val BreathInSeconds = DesignTokens.Motion.breathInMs / 1000f
private val BreathOutSeconds = DesignTokens.Motion.breathOutMs / 1000f
private val BreathCycleSeconds = BreathInSeconds + BreathOutSeconds

/**
 * Countdown ring with a soft breath pulse as a visual anchor during the sit.
 */
@Composable
fun TimerRing(
    secondsRemaining: Int,
    totalSeconds: Int,
    isBreathing: Boolean,
    isDone: Boolean = false,
    modifier: Modifier = Modifier
) {
    val total = totalSeconds.coerceAtLeast(1).toFloat()
    val finished = isDone
    val motion = MaterialTheme.motionScheme
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = if (finished) {
        DesignTokens.Semantic.success
    } else {
        DesignTokens.Semantic.sit
    }
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val surface = MaterialTheme.colorScheme.surface

    val animatedProgress = remember { Animatable(secondsRemaining / total) }
    val breathScale = remember { Animatable(1f) }
    val breathGlow = remember { Animatable(0.35f) }
    val breathLift = remember { Animatable(0f) }
    var announceText by remember {
        mutableStateOf(countdownAnnouncement(secondsRemaining, totalSeconds, finished))
    }

    LaunchedEffect(secondsRemaining, totalSeconds, finished) {
        val shouldAnnounce = finished ||
            secondsRemaining == totalSeconds ||
            secondsRemaining % AnnounceEverySeconds == 0
        if (shouldAnnounce) {
            announceText = countdownAnnouncement(secondsRemaining, totalSeconds, finished)
        }
    }

    LaunchedEffect(secondsRemaining, isBreathing, totalSeconds, finished) {
        if (finished || secondsRemaining == 0) {
            animatedProgress.animateTo(
                targetValue = 0f,
                animationSpec = motion.defaultEffectsSpec()
            )
            return@LaunchedEffect
        }

        val current = secondsRemaining / total
        if (!isBreathing) {
            animatedProgress.snapTo(current)
            return@LaunchedEffect
        }

        if (abs(animatedProgress.value - current) > 0.02f) {
            animatedProgress.snapTo(current)
        }

        val next = (secondsRemaining - 1).coerceAtLeast(0) / total
        animatedProgress.animateTo(
            targetValue = next,
            animationSpec = tween(
                durationMillis = DesignTokens.Motion.timerTickMs.toInt(),
                easing = LinearEasing
            )
        )
    }

    var lastActiveScale by remember { mutableFloatStateOf(BreathScaleMin) }
    var lastActiveGlow by remember { mutableFloatStateOf(0.2f) }
    var lastActiveLift by remember { mutableFloatStateOf(0f) }

    val progress = animatedProgress.value
    val elapsedSeconds = total * (1f - progress)
    val cyclePos = elapsedSeconds.mod(BreathCycleSeconds)
    val breathPhase = breathPhaseFor(cyclePos)

    val scale: Float
    val glow: Float
    val lift: Float
    if (finished) {
        scale = breathScale.value
        glow = breathGlow.value
        lift = breathLift.value
    } else {
        scale = BreathScaleMin + (BreathScaleMax - BreathScaleMin) * breathPhase
        glow = 0.2f + (0.62f - 0.2f) * breathPhase
        lift = breathPhase
    }

    SideEffect {
        if (!finished) {
            lastActiveScale = scale
            lastActiveGlow = glow
            lastActiveLift = lift
        }
    }

    LaunchedEffect(finished) {
        if (finished) {
            breathScale.snapTo(lastActiveScale)
            breathGlow.snapTo(lastActiveGlow)
            breathLift.snapTo(lastActiveLift)
            coroutineScope {
                launch {
                    breathScale.animateTo(1f, motion.defaultSpatialSpec())
                }
                launch {
                    breathGlow.animateTo(
                        targetValue = 0.55f,
                        animationSpec = motion.defaultEffectsSpec()
                    )
                }
                launch {
                    breathLift.animateTo(0f, motion.defaultEffectsSpec())
                }
            }
        }
    }

    val centerLabel = if (finished) "0" else secondsRemaining.toString()

    Box(
        modifier = modifier
            .size(DesignTokens.Components.timerRingSize)
            .semantics(mergeDescendants = true) {
                liveRegion = LiveRegionMode.Polite
                contentDescription = announceText
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        ) {
            val strokeWidth = DesignTokens.Components.timerRingStrokeWidth.toPx()
            val radius = (size.minDimension - strokeWidth) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            drawCircle(
                color = progressColor.copy(alpha = 0.11f * glow),
                radius = radius + strokeWidth * 2.8f,
                center = center
            )

            val washCenter = Offset(
                x = center.x,
                y = center.y + radius * 0.32f * (1f - 2f * lift)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        progressColor.copy(alpha = 0.18f * glow),
                        progressColor.copy(alpha = 0.05f * glow),
                        progressColor.copy(alpha = 0f)
                    ),
                    center = washCenter,
                    radius = radius * 0.92f
                ),
                radius = radius * 0.92f,
                center = center
            )

            drawCircle(
                color = surface.copy(alpha = 0.55f),
                radius = radius * (0.34f + 0.04f * glow),
                center = center
            )

            val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )

            if (progress > 0.001f) {
                rotate(degrees = -90f, pivot = center) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                progressColor.copy(alpha = 0.82f),
                                progressColor
                            ),
                            center = center
                        ),
                        startAngle = 0f,
                        sweepAngle = 360f * progress,
                        useCenter = false,
                        style = stroke
                    )
                }
            }
        }

        AnimatedContent(
            targetState = centerLabel,
            modifier = Modifier.align(Alignment.Center),
            transitionSpec = {
                (
                    fadeIn(animationSpec = motion.fastEffectsSpec()) +
                        scaleIn(
                            initialScale = 0.86f,
                            animationSpec = motion.fastSpatialSpec()
                        )
                    ) togetherWith (
                    fadeOut(animationSpec = motion.fastEffectsSpec()) +
                        scaleOut(
                            targetScale = 1.08f,
                            animationSpec = motion.fastSpatialSpec()
                        )
                    )
            },
            label = "timerCount"
        ) { value ->
            Text(
                text = value,
                style = MaterialTheme.typography.displayLarge,
                color = onBackground,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = when {
                finished -> "complete"
                isBreathing -> "seconds"
                else -> "paused"
            },
            style = MaterialTheme.typography.labelLarge,
            color = onVariant.copy(alpha = 0.85f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = DesignTokens.Spacing.xl)
        )
    }
}

private fun easeInOut(t: Float): Float {
    val clamped = t.coerceIn(0f, 1f)
    return (1f - cos(clamped * Math.PI.toFloat())) / 2f
}

private fun breathPhaseFor(cyclePosSeconds: Float): Float =
    if (cyclePosSeconds < BreathInSeconds) {
        easeInOut(cyclePosSeconds / BreathInSeconds)
    } else {
        1f - easeInOut((cyclePosSeconds - BreathInSeconds) / BreathOutSeconds)
    }

private fun countdownAnnouncement(
    secondsRemaining: Int,
    totalSeconds: Int,
    isDone: Boolean
): String =
    when {
        isDone -> "One minute of silence is complete."
        secondsRemaining == totalSeconds -> "$secondsRemaining seconds of silence. Close your eyes."
        else -> "$secondsRemaining seconds remaining"
    }
