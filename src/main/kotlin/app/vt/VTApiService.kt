package app.vt

import retrofit2.Call
import retrofit2.http.*

interface VTApiService {

    @POST("urls")
    @FormUrlEncoded
    fun analyzeUrl(@Field("url") url: String): Call<VTResponse<AnalyzeUrl>>

    @GET("analyses/{id}")
    fun getAnalyseResult(@Path("id") analyseId: String): Call<VTResponse<AnalyzeResult>>

}
