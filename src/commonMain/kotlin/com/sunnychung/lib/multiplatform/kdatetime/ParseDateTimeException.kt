package com.sunnychung.lib.multiplatform.kdatetime

class ParseDateTimeException(message: String? = null) : RuntimeException(message ?: "Input date/time is invalid or does not match with expected pattern")
