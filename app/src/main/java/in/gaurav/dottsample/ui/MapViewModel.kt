package `in`.gaurav.dottsample.ui

import `in`.gaurav.dottsample.dal.Repository
import `in`.gaurav.dottsample.model.Venue
import `in`.gaurav.dottsample.util.DistanceUtility
import `in`.gaurav.dottsample.util.MIN_MAP_ZOOM_TO_AUTO_FETCH
import android.location.Location
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import `in`.gaurav.dottsample.model.Location as LocationModel

class MapViewModel : ViewModel() {
    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var distanceUtility: DistanceUtility

    private var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)


    private var lastLocation: MutableLiveData<android.location.Location?> = MutableLiveData(null)


    private var lastVenue: MutableLiveData<Venue?> = MutableLiveData(null)

    private var mapZoom: Float = 0.0f

    @VisibleForTesting
    var lastFetchedLocation: LocationModel = LocationModel()


    fun observeVenueData(owner: LifecycleOwner, observer: Observer<List<Venue>>) {
        viewModelScope.launch {
            repository.loadCached().observe(owner, observer)
        }
    }

    fun observeLoader(owner: LifecycleOwner, observer: Observer<Boolean>) {
        isLoading.observe(owner, observer)
    }

    fun observeLastLocation(owner: LifecycleOwner, observer: Observer<android.location.Location?>) {
        lastLocation.removeObserver(observer)
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(
                    {
                        lastLocation.postValue(it.result)
                        lastLocation.observe(owner, observer)
                        onLocationFetched(it.result)
                    })
            }
        }
    }

    fun observeLastVenue(owner: LifecycleOwner, observer: Observer<Venue?>) {
        lastVenue.observe(owner, observer)
    }

    private fun onLocationFetched(it: Location?) {
        if (repository.totalVenues() == 0) {
            it?.let {
                fetchNearPlaces(
                    LocationModel(
                        it.latitude, it.longitude
                    )
                )
            }
        }
    }

    fun onMapMoved(mapZoom: Float, latLng: LatLng) {
        this.mapZoom = mapZoom
        if (isNewLocationUnderFetchThreshold(mapZoom, latLng)) {
            fetchNearPlaces(LocationModel(latLng.latitude, latLng.longitude))
        }
    }

    @VisibleForTesting
    fun isNewLocationUnderFetchThreshold(mapZoom: Float, location: LatLng): Boolean {
        if (mapZoom < MIN_MAP_ZOOM_TO_AUTO_FETCH) {
            // map is zoomed out a lot, no point fetching more
            return false
        }

        if (lastFetchedLocation.lat == 0.0) {
            // corner case, first time
            return true
        }

        val distance = distanceUtility.getDistancebetween(location, lastFetchedLocation)
        println("Distance between old and new location is $distance metres and mapzoom is $mapZoom")
        return distanceUtility.isDistanceUnderThreshold(distance, mapZoom)
    }


    fun fetchNearPlaces(location: LocationModel) {
        if (isLoading.value == true) {
            println("Silently Ignore, One call in progress")
        } else {
            isLoading.value = true
            viewModelScope.launch {
                lastFetchedLocation = location
                println("Loading started")
                repository.loadNearByPlaces(location)
                isLoading.value = false
            }
        }
    }

    fun onVenueSelected(venue: Venue) {
        lastVenue.value = venue
    }


}