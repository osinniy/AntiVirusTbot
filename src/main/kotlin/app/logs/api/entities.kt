package app.logs.api

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock.System as SystemClock

@Serializable data class Report(
    val eventTime: String = SystemClock.now().toString(),
    val serviceContext: ServiceContext,
    val message: String, /* Throwable#printStackTrace() */
    val context: ErrorContext
)

@Serializable data class ServiceContext(
    val service: String,
    val version: String,
)

@Serializable data class ErrorContext(
    val user: String
)
