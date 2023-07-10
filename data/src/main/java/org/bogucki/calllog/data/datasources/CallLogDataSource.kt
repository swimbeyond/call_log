package org.bogucki.calllog.data.datasources

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.CallLog
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bogucki.calllog.domain.models.CallLogEntry
import java.time.Instant

internal class CallLogDataSource(
    private val resolver: ContentResolver,
    private val scope: CoroutineScope
) {

    private val uri = CallLog.Calls.CONTENT_URI

    private val _callLogFlow = MutableStateFlow(listOf<CallLogEntry>())

    val callLog: StateFlow<List<CallLogEntry>> = _callLogFlow

    private var counter = 0
    fun incrementCounter() {
        counter++
    }

    init {
        observeCallLog()
    }

    /**
     * When new call is detected by content observer we check for the latest call, assign the counter value.
     * Then we reset the counter and emit the flow with the updated call log list
     */
    private fun observeCallLog() = try {
        resolver.registerContentObserver(
            uri,
            true,
            object : ContentObserver(Handler(Looper.getMainLooper())) {
                override fun onChange(selfChange: Boolean, uris: Collection<Uri>, flags: Int) {

                    scope.launch {
                        val call = getLastCall()
                        val newList = _callLogFlow.value.toMutableList()
                            //The onChange callback is triggered more times when launching the phone app.
                            // Therefore we need to prevent from adding duplicates
                            .apply { if (contains(call).not()) add(0, call) }
                        _callLogFlow.emit(newList)
                    }
                }
            })
    } catch (ex: SecurityException) {
        Log.e(this::class.simpleName, "Missing permissions for call log", ex)
    }

    fun getLastCall(): CallLogEntry {
        val projection = arrayOf(
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION
        )

        val bundle = Bundle().apply {
            putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(CallLog.Calls.DATE))
            putInt(
                ContentResolver.QUERY_ARG_SORT_DIRECTION,
                ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
            )
        }

        val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            resolver.query(
                CallLog.Calls.CONTENT_URI.buildUpon()
                    .appendQueryParameter(CallLog.Calls.LIMIT_PARAM_KEY, "1")
                    .build(), projection, bundle, null
            ) ?: throw Exception("Unable to create the query")
        } else {
            resolver.query(
                CallLog.Calls.CONTENT_URI.buildUpon()
                    .appendQueryParameter(CallLog.Calls.LIMIT_PARAM_KEY, "1").build(),
                projection,
                null,
                null,
                CallLog.Calls.DATE
            ) ?: throw Exception("Unable to create the query")
        }

        val numberColumn: Int = cursor.getColumnIndex(CallLog.Calls.NUMBER)
        val durationColumn: Int = cursor.getColumnIndex(CallLog.Calls.DURATION)
        val dateColumn: Int = cursor.getColumnIndex(CallLog.Calls.DATE)
        val nameColumn: Int = cursor.getColumnIndex((CallLog.Calls.CACHED_NAME))

        if (cursor.moveToFirst()) {
            val number = cursor.getString(numberColumn)
            val date = cursor.getString(dateColumn)
            val beginning = Instant.ofEpochMilli(date.toLong())
            val duration = cursor.getString(durationColumn)
            val name = cursor.getString(nameColumn)

            val call =
                CallLogEntry(
                    beginning = beginning,
                    number = number,
                    name = if (name != "null") name else null,
                    duration = duration.toLong(),
                ).apply { timesQueried = counter }
            counter = 0
            cursor.close()
            return call
        }

        throw IllegalStateException("Could not get last call log entry")
    }
}