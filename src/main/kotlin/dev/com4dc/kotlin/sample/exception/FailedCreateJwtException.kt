package dev.com4dc.kotlin.sample.exception

data class FailedCreateJwtException(val msg: String, val throwable: Throwable? = null): RuntimeException(msg, throwable)