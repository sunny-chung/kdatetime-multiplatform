package com.sunnychung.lib.multiplatform.kdatetime

internal actual fun kInstantOfCurrentTime(): KInstant {
    return KInstant(System.currentTimeMillis())
}
