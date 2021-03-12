package app

import app.logs.api.CloudReportingApiService
import app.vt.VTApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create

@Suppress("EXPERIMENTAL_API_USAGE")
object Apis {
    private val kotlinxSerializationConverterFactory = Json { ignoreUnknownKeys = true }
        .asConverterFactory(MediaType.get("application/json"))

    private val vtRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.virustotal.com/api/v3/")
        .addConverterFactory(kotlinxSerializationConverterFactory)
        .client(OkHttpClient.Builder().addInterceptor { chain ->
            chain.request().newBuilder().addHeader("x-apikey", config.vtApiKey).build().let { chain.proceed(it) }
        }.build())
        .build()

    val vt = vtRetrofit.create<VTApiService>()

    private val cloudReportingRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://clouderrorreporting.googleapis.com/v1beta1/projects/")
        .addConverterFactory(kotlinxSerializationConverterFactory)
        .build()

    val cloudReporting = cloudReportingRetrofit.create<CloudReportingApiService>()
}
