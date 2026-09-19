package com.learnfromsilence.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.learnfromsilence.app.ui.design.DesignTokens
import kotlinx.coroutines.delay

const val DefaultSilenceDurationSeconds = 60
const val MinSilenceDurationSeconds = 30
const val SilenceDurationStepSeconds = 30

enum class SitLengthFeedback {
    TooShort,
    Enough,
    TooLong
}

@Stable
class SilenceSessionState(
    targetDurationSeconds: Int = DefaultSilenceDurationSeconds,
    secondsRemaining: Int = targetDurationSeconds,
    isDone: Boolean = false,
    isRunning: Boolean = false,
    awaitingFeedback: Boolean = false,
    runId: Int = 0
) {
    var targetDurationSeconds by mutableIntStateOf(targetDurationSeconds)
        private set
    var secondsRemaining by mutableIntStateOf(secondsRemaining)
        private set
    var isDone by mutableStateOf(isDone)
        private set
    var isRunning by mutableStateOf(isRunning)
        private set
    var awaitingFeedback by mutableStateOf(awaitingFeedback)
        private set
    var runId by mutableIntStateOf(runId)
        private set

    val isIdle: Boolean
        get() = !isRunning && !isDone && !awaitingFeedback &&
            secondsRemaining == targetDurationSeconds

    val elapsedSeconds: Int
        get() = targetDurationSeconds - secondsRemaining

    fun start() {
        if (isDone || awaitingFeedback) return
        isRunning = true
    }

    fun pause() {
        isRunning = false
    }

    fun pauseOrResume() {
        if (isDone || awaitingFeedback) return
        isRunning = !isRunning
    }

    fun restart() {
        secondsRemaining = targetDurationSeconds
        isDone = false
        awaitingFeedback = false
        isRunning = true
        runId += 1
    }

    fun resetIdle() {
        secondsRemaining = targetDurationSeconds
        isDone = false
        awaitingFeedback = false
        isRunning = false
        runId += 1
    }

    fun applyFeedback(feedback: SitLengthFeedback) {
        if (!awaitingFeedback) return

        targetDurationSeconds = when (feedback) {
            SitLengthFeedback.TooShort ->
                targetDurationSeconds + SilenceDurationStepSeconds
            SitLengthFeedback.Enough ->
                targetDurationSeconds
            SitLengthFeedback.TooLong ->
                (targetDurationSeconds - SilenceDurationStepSeconds)
                    .coerceAtLeast(MinSilenceDurationSeconds)
        }

        secondsRemaining = targetDurationSeconds
        isDone = false
        awaitingFeedback = false
        isRunning = false
        runId += 1
    }

    internal fun tick() {
        if (isDone || awaitingFeedback || !isRunning) return
        if (secondsRemaining <= 0) return

        secondsRemaining -= 1
        if (secondsRemaining == 0) {
            isDone = true
            isRunning = false
            awaitingFeedback = true
        }
    }

    companion object {
        val Saver: Saver<SilenceSessionState, Any> = listSaver(
            save = {
                listOf(
                    it.targetDurationSeconds,
                    it.secondsRemaining,
                    if (it.isDone) 1 else 0,
                    if (it.isRunning) 1 else 0,
                    if (it.awaitingFeedback) 1 else 0,
                    it.runId
                )
            },
            restore = {
                SilenceSessionState(
                    targetDurationSeconds = it[0],
                    secondsRemaining = it[1],
                    isDone = it[2] == 1,
                    isRunning = it[3] == 1,
                    awaitingFeedback = it[4] == 1,
                    runId = it[5]
                )
            }
        )
    }
}

fun formatSitDurationLabel(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return when {
        minutes == 0 -> "$seconds seconds"
        seconds == 0 && minutes == 1 -> "one minute"
        seconds == 0 -> "$minutes minutes"
        minutes == 1 -> "1 min $seconds sec"
        else -> "$minutes min $seconds sec"
    }
}

fun formatBeginSitLabel(totalSeconds: Int): String =
    "Begin ${formatSitDurationLabel(totalSeconds)}"

@Composable
fun rememberSilenceSession(): SilenceSessionState {
    val session = rememberSaveable(saver = SilenceSessionState.Saver) {
        SilenceSessionState()
    }

    LaunchedEffect(session.runId, session.isRunning) {
        if (!session.isRunning) return@LaunchedEffect

        while (true) {
            if (session.isDone || session.awaitingFeedback) break
            delay(DesignTokens.Motion.timerTickMs)
            session.tick()
            if (session.isDone || session.awaitingFeedback) break
        }
    }

    return session
}
