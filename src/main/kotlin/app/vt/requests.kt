package app.vt

import app.Apis
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.Call
import retrofit2.awaitResponse
import kotlin.time.ExperimentalTime
import kotlin.time.minutes
import kotlin.time.seconds

@Suppress("UNREACHABLE_CODE", "ThrowableNotThrown")
@ExperimentalTime
suspend fun analyzeUrl(url: String): Pair<AnalyzeResultAttributesStats?, Throwable?>? {
    return proceedVTCall {
        Apis.vt.analyzeUrl(url)
    }?.let { analyzeUrlCallResult ->
        val (analyzeUrl, exception1) = analyzeUrlCallResult
        if (exception1 != null) return@let null to exception1

        withTimeoutOrNull(5.minutes) {
            while (true) {
                proceedVTCall {
                    Apis.vt.getAnalyseResult(analyzeUrl!!.id)
                }?.let { analyseCallResult ->
                    val (analyze, exception2) = analyseCallResult
                    if (exception2 != null) return@withTimeoutOrNull null to exception2

                    if (analyze!!.attributes.status == AnalyzeResultStatus.COMPLETED) {
                        return@withTimeoutOrNull analyze.attributes.stats to null /* success */
                    }
                } ?: return@withTimeoutOrNull null

//                TODO: what should i do with this???
                delay(10.seconds) /* non production ready */
            }
            throw RuntimeException() /* never thrown */
        }
    }
}

@ExperimentalTime
private suspend fun <R> proceedVTCall(obtainer: () -> Call<VTResponse<R>>): Pair<R?, Throwable?>? {
    return try {
        withTimeoutOrNull(5.minutes) {
            obtainer().proceed() to null
        }
    } catch (t: Throwable) {
        t.printStackTrace()
        null to t
    }
}

@Throws(VTException::class)
@ExperimentalTime
private suspend fun <R> Call<VTResponse<R>>.proceed(): R {
    while (true) {
        val response = awaitResponse()
        if (response.isSuccessful) {
            return response.body()!!.data
        } else {
            val error = response.errorBody()!!.asVTError()
            when (error.code) {
                VTErrorCode.QUOTA_EXCEEDED, VTErrorCode.TOO_MANY_REQUESTS -> delay(60.seconds)
                else -> throw VTException(error)
            }
        }
    }
}
