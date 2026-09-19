package com.learnfromsilence.app.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.learnfromsilence.app.R

/**
 * Soft chime once when a sit completes. Silent during the countdown itself.
 */
@Composable
fun SilenceEndChimeEffect(session: SilenceSessionState) {
    val context = LocalContext.current
    val feedback = remember(context) { SilenceEndChime(context) }
    var previousDone by remember { mutableStateOf(session.isDone) }

    DisposableEffect(feedback) {
        onDispose { feedback.release() }
    }

    LaunchedEffect(session.isDone, session.runId) {
        val justFinished = session.isDone && !previousDone
        previousDone = session.isDone
        if (justFinished) {
            feedback.playChime()
        }
    }
}

private class SilenceEndChime(context: Context) {
    private val appContext = context.applicationContext
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()
    private var loaded = false
    private val chimeId = soundPool.load(appContext, R.raw.chime, 1)

    init {
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0 && sampleId == chimeId) {
                loaded = true
            }
        }
    }

    fun playChime() {
        if (!loaded) return
        soundPool.play(chimeId, 0.7f, 0.7f, 1, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }
}
