package org.bogucki.calllog.domain.models

import java.time.Instant

data class CallLogEntry(
    val beginning: Instant,
    val number: String,
    val name: String?,
    val duration: Long,
) {
    var timesQueried: Int = 0
}
