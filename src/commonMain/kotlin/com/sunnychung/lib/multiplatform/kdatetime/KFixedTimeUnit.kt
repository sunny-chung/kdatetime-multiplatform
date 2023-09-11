package com.sunnychung.lib.multiplatform.kdatetime//open class KTimeUnit(val value: Long, val ratioToMillis: Long) : KDuration(value * ratioToMillis) {
//
////    override fun toMilliseconds(): Long = value * ratioToMillis
//    fun fromMilliseconds(millis: Long): Long = millis / ratioToMillis
//
//    fun toTimeUnit(unit: KTimeUnit)
//
//}
//
//class KMillisecond(value: Long) : KTimeUnit(value, 1)
//class KSecond(value: Long) : KTimeUnit(value, 1000L)
//class KMinute(value: Long) : KTimeUnit(value, 1000L * 60L)
//class KHour(value: Long) : KTimeUnit(value, 1000L * 60L * 60L)
//class KDay(value: Long) : KTimeUnit(value, 1000L * 60L * 60L * 24L)

enum class KFixedTimeUnit(val ratioToMillis: Long) {
    MilliSecond(1L),
    Second(1000L),
    Minute(1000L * 60L),
    Hour(1000L * 60L * 60L),
    Day(1000L * 60L * 60L * 24L),
    Week(1000L * 60L * 60L * 24L * 7L)

//    fun toMilliseconds(value: Long) = value * ratioToMillis
//    fun fromMilliseconds(millis: Long): Long = millis / ratioToMillis
}
