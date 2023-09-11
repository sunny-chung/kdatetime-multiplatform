package com.sunnychung.lib.multiplatform.kdatetime

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.posix.gettimeofday
import platform.posix.timeval

internal actual fun kInstantOfCurrentTime(): KInstant {
    val timestampMs = memScoped {
        val timeVal = alloc<timeval>()
        gettimeofday(timeVal.ptr, null)
        (timeVal.tv_sec * 1000) + (timeVal.tv_usec / 1000)
    }
    return KInstant(timestampMs)
}