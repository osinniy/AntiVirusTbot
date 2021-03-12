package app.logs

import app.Apis
import app.config
import app.logs.api.ErrorContext
import app.logs.api.Report
import app.logs.api.ServiceContext
import com.google.auth.oauth2.ServiceAccountCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException

object Logger {

    private val credentials = try {
        if (config.gcloudProjectName == null) null
        else ServiceAccountCredentials.fromStream(File("cloud-logging-credentials.json").inputStream())
            .createScoped("https://www.googleapis.com/auth/cloud-platform",
                "https://www.googleapis.com/auth/stackdriver-integration")
    } catch (e: FileNotFoundException) {
        println("'cloud-logging-credentials.json' not found. Logging is disabled")
        null
    }

    fun log(t: Throwable, userId: Any? = null) {
        if (credentials == null) return

        GlobalScope.launch(Dispatchers.IO) {
            val report = Report(
                serviceContext = ServiceContext(
                    service = "bot-server",
                    version = "unspecified"
                ),
                message = t.stackTraceToString(),
                context = ErrorContext(
                    user = (userId ?: "").toString()
                )
            )

            credentials.refreshIfExpired()

            val response = Apis.cloudReporting.report(
                config.gcloudProjectName!!,
                credentials.accessToken.tokenValue,
                report
            ).execute()

            if (!response.isSuccessful) {
                println("Failed to send logs because of: ${response.errorBody()!!.string()}")
            }
        }
    }

}
