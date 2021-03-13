package app.vt

import app.Apis
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonElement
import okhttp3.ResponseBody

@Serializable data class VTResponse<T>(val data: T? = null, val error: VTError? = null)

@Serializable data class VTError(val code: VTErrorCode, val message: String)

class VTException(error: VTError) : RuntimeException(error.message)

fun ResponseBody.asVTError() =
    Apis.kotlinxSerialization.decodeFromString<VTResponse<JsonElement>>(string()).error!!

/****** analyzeUrl ******/

@Serializable data class AnalyzeUrl(
    val type: String,
    val id: String
)

/****** getAnalyseResult ******/

@Serializable data class AnalyzeResult(
    val attributes: AnalyzeResultAttributes,
    val id: String,
    val type: String
)

@Serializable data class AnalyzeResultAttributes(
    val date: Int,
    val stats: AnalyzeResultAttributesStats,
    val status: AnalyzeResultStatus
)

@Serializable data class AnalyzeResultAttributesStats(
    val harmless: Int,
    val malicious: Int,
    val suspicious: Int,
    val timeout: Int,
    val undetected: Int
)

/****** enums ******/

@Suppress("unused")
@Serializable enum class VTErrorCode {
    @SerialName("BadRequestError") BAD_REQUEST,
    @SerialName("InvalidArgumentError") INVALID_ARGUMENT,
    @SerialName("NotAvailableYet") NOT_AVAILABLE_YET,
    @SerialName("UnselectiveContentQueryError") UNSELECTIVE_CONTENT_QUERY,
    @SerialName("UnsupportedContentQueryError") UNSUPPORTED_CONTENT_QUERY,
    @SerialName("AuthenticationRequiredError") AUTHENTICATION_REQUIRED,
    @SerialName("UserNotActiveError") USER_NOT_ACTIVE,
    @SerialName("WrongCredentialsError") WRONG_CREDENTIALS,
    @SerialName("ForbiddenError") FORBIDDEN,
    @SerialName("NotFoundError") NOT_FOUND,
    @SerialName("AlreadyExistsError") ALREADY_EXISTS,
    @SerialName("FailedDependencyError") FAILED_DEPENDENCY,
    @SerialName("QuotaExceededError") QUOTA_EXCEEDED,
    @SerialName("TooManyRequestsError") TOO_MANY_REQUESTS,
    @SerialName("TransientError") TRANSIENT,
    @SerialName("DeadlineExceededError") DEADLINE_EXCEEDED
}

@Serializable enum class AnalyzeResultStatus {
    @SerialName("completed") COMPLETED,
    @SerialName("queued") QUEUED,
    @SerialName("in-progress") IN_PROGRESS
}
