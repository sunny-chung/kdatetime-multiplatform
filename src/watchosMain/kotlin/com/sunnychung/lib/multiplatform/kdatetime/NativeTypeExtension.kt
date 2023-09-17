package com.sunnychung.lib.multiplatform.kdatetime

actual fun <R : Number> Long.toNSInteger(): R = toInt() as R
