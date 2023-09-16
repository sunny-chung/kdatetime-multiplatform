package com.sunnychung.lib.multiplatform.kdatetime

import com.sunnychung.lib.multiplatform.kdatetime.annotation.AndroidParcelize

/**
 * @param year
 * @param month 1-indexed
 * @param day 1-indexed
 */
@AndroidParcelize
data class KDate(val year: Int, val month: Int, val day: Int) : AndroidParcelable
