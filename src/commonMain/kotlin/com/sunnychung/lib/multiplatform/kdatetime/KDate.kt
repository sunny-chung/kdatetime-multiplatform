package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.annotation.AndroidParcelize

/**
 * @param year
 * @param month 1-indexed
 * @param day 1-indexed
 */
@AndroidParcelize
data class KDate(val year: Int, val month: Int, val day: Int) : AndroidParcelable {
    init {
        // TODO: allow other calendars
        try {
            KGregorianCalendar.validateDate(year = year, month = month, day = day)
        } catch (e: Error) {
            println("$this is invalid")
            throw e
        }
    }
}
