package `in`.gaurav.dottsample.dal

import `in`.gaurav.dottsample.model.Location
import `in`.gaurav.dottsample.model.Venue
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository(
    private val localDataSource: LocalDataSource,
    private val networkDataSource: NetworkDataSource
) {


    fun totalVenues(): Int = localDataSource.totalVenues()

    suspend fun loadCached(): MutableLiveData<List<Venue>> {
        return localDataSource.loadAll()
    }

    suspend fun loadNearByPlaces(location: Location) {
        // TODO: purge local cached data after a while
        return withContext(Dispatchers.IO) {
            val nearbyPlaces = networkDataSource.loadNearPlaces(location)
            // TODO error handling
            if (nearbyPlaces.isNotEmpty()) localDataSource.insertAll(nearbyPlaces)
        }
    }
}