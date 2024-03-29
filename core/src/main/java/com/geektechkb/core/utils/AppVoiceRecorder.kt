package com.geektechkb.core.utils

import android.media.MediaRecorder
import android.util.Log
import com.geektechkb.core.extensions.generateRandomId
import java.io.File
import java.io.IOException

class AppVoiceRecorder {

    private lateinit var voiceMessageFile: File
    private var voiceMessageFileName: String = ""
    private val mediaRecorder = MediaRecorder()

    fun retrieveVoiceMessageFile() = voiceMessageFile
    fun createFileForRecordedVoiceMessage(externalFilesDir: File?) {
        voiceMessageFileName += "/Audio${generateRandomId()}.mp3"
        Log.e("anime", "externalFile: ${externalFilesDir}$voiceMessageFileName")
        voiceMessageFile = File(externalFilesDir, voiceMessageFileName)
    }

    fun startRecordingVoiceMessage() {
        mediaRecorder.apply {
            actionWhenManipulatingWithVoiceMessage(actionWhenRecording = {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(voiceMessageFile.absolutePath)
                prepare()
                start()
            })
        }
    }

    fun stopRecordingVoiceMessage() = with(mediaRecorder) {
        actionWhenManipulatingWithVoiceMessage(actionWhenStoppingRecording = {
            stop()
            reset()
            release()
        }, actionWhenCaughtAnException = {
            voiceMessageFile.delete()
        })
    }

    private fun actionWhenManipulatingWithVoiceMessage(
        actionWhenRecording: (() -> Unit)? = null,
        actionWhenStoppingRecording: (() -> Unit)? = null,
        actionWhenCaughtAnException: (() -> Unit)? = null
    ) {
        try {
            actionWhenRecording?.invoke()
            actionWhenStoppingRecording?.invoke()
        } catch (e: IOException) {
            Log.e("gaypop", e.message.toString())
            actionWhenCaughtAnException?.invoke()
        } catch (e: Exception) {
            Log.e("gaypop", e.message.toString())
            actionWhenCaughtAnException?.invoke()
        }
    }

    fun deleteRecordedVoiceMessage() {
        voiceMessageFile.delete()
    }
}