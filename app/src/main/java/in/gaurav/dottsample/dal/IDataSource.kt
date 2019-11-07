package `in`.gaurav.dottsample.dal

import `in`.gaurav.dottsample.model.Location
import `in`.gaurav.dottsample.model.Venue
import `in`.gaurav.dottsample.network.FourSquareAPI
import androidx.lifecycle.MutableLiveData

class LocalDataSource(val data:MutableLiveData<List<Venue>> ) {

    fun loadAll(): MutableLiveData<List<Venue>> {
        return data
    }

    fun insertAll(venues: List<Venue>) {
        val allVenues = mutableListOf<Venue>()
        allVenues.addAll(venues)
        allVenues.addAll(data?.value ?: emptyList())
        data.postValue(allVenues)
    }

    fun deleteAll() {
        data.postValue(emptyList())
    }

    fun totalVenues(): Int = data.value?.size ?: 0
}

class NetworkDataSource(private val api: FourSquareAPI) {

    suspend fun loadNearPlaces(location: Location): List<Venue> {
        try {
            val response =
                api.loadNearbyPlaces(latlng = location.lat.toString() + "," + location.lng)
            if (response.isSuccessful) {
                //TODO error handling and direct gson to Venue
                return response.body()?.response?.venueList ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }
}