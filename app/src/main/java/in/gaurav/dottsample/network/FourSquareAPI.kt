package `in`.gaurav.dottsample.network

import `in`.gaurav.dottsample.util.CLIENT_ID
import `in`.gaurav.dottsample.util.CLIENT_SECRET
import `in`.gaurav.dottsample.util.END_POINT
import `in`.gaurav.dottsample.util.VERSION
import `in`.gaurav.dottsample.model.FoursquareResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FourSquareAPI {

    @GET(END_POINT)
    suspend fun loadNearbyPlaces(
        @Query("v") v: Int = VERSION,
        @Query("client_id") client_id: String = CLIENT_ID,
        @Query("client_secret") client_secret: String = CLIENT_SECRET,
        @Query("ll") latlng:String
    ): Response<FoursquareResponse>
}