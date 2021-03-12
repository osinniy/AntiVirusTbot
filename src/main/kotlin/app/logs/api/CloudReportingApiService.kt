package app.logs.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CloudReportingApiService {

    @POST("{projectName}/events:report")
    fun report(
        @Path("projectName") projectName: String,
        @Query("access_token") oauthToken: String,
        @Body report: Report
    ): Call<Unit>

}
