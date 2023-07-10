package org.bogucki.calllog.extensions

import android.util.Log

fun Any.logd(message: String) = Log.d(this::class.java.simpleName, message)
fun Any.loge(message: String, ex: Throwable? = null) =
    Log.e(this::class.java.simpleName, message, ex)

/**
 * Converts a number of seconds into a time string in format HH:mm:ss
 */
fun Long.secondstoHHmmssString(): String {
    val hours = this / 3600
    val minutes = ((this - hours * 3600) / 60).toString().padStart(2, '0')
    val seconds = (this % 60).toString().padStart(2, '0')

    return if (hours == 0L)
        "$minutes:$seconds"
    else
        "$hours:$minutes:$seconds"
}