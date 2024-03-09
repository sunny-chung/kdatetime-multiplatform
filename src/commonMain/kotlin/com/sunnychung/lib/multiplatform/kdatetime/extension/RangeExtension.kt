package com.sunnychung.lib.multiplatform.kdatetime.extension

import com.sunnychung.lib.multiplatform.kdatetime.KPointOfTime

operator fun <A: KPointOfTime, B: KPointOfTime> ClosedRange<A>.contains(other: B): Boolean {
    return this.start <= other && other <= this.endInclusive
}

@OptIn(ExperimentalStdlibApi::class)
operator fun <A: KPointOfTime, B: KPointOfTime> OpenEndRange<A>.contains(other: B): Boolean {
    return this.start <= other && other < this.endExclusive
}
