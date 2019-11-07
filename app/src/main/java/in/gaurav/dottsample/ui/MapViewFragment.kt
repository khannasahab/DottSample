package `in`.gaurav.dottsample.ui

import `in`.gaurav.dottsample.R
import `in`.gaurav.dottsample.model.Venue
import `in`.gaurav.dottsample.util.DEFAULT_MAP_ZOOM
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class MapViewFragment : SupportMapFragment(), OnMapReadyCallback {

    companion object {
        fun getInstance() = MapViewFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInjection()
        getMapAsync(this)
    }

    @Inject
    lateinit var mapViewModel: MapViewModel

    private lateinit var mMap: GoogleMap


    private fun initInjection() {
        (activity as MapsActivity).daggerComponent().inject(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initObserver()
        initMarkerClickListener()
        // Add a marker in Sydney and move the camera

    }

    private fun initMarkerClickListener() {
        mMap.setOnMarkerClickListener {
            mapViewModel.onVenueSelected(it!!.tag as Venue)
            true
        }
    }

    private fun initObserver() {
        observeVenueContent()

        observeLoader()

        observeLocation()
    }

    private fun observeLocation() {
        mapViewModel.observeLastLocation(this, Observer {
            if (it == null) {
                onLastLocationFetchFailed()
            } else {
                onLastLocationFetched(it)
            }
        })
    }

    private fun observeLoader() {
        mapViewModel.observeLoader(this, Observer {
            if (it) {
                Toast.makeText(activity, R.string.loading, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observeVenueContent() {
        mapViewModel.observeVenueData(this, Observer {
            if (it.isEmpty()) {
                println("Silently fail, no markers loaded")
            } else {
                println("Loaded New Markers")
                updateMarkers(it!!)
            }
        })
    }

    private fun updateMarkers(venues: List<Venue>) {
        venues.forEach {
            val venueLocation = LatLng(it.location.lat, it.location.lng)
            val marker = mMap.addMarker(
                MarkerOptions().position(venueLocation).title(it.name)
            )
            marker.tag = it
        }
    }

    private fun onLastLocationFetchFailed() {
        // show error dialog
        Toast.makeText(activity, R.string.location_failed, Toast.LENGTH_LONG).show()
    }

    private fun onLastLocationFetched(lastLocation: Location) {
        initMapMoveListener()
        initInitalLocationonMap(lastLocation)
    }

    private fun initInitalLocationonMap(lastLocation: Location) {
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    lastLocation.latitude,
                    lastLocation.longitude
                ), DEFAULT_MAP_ZOOM
            )
        )
    }

    private fun initMapMoveListener() {

        mMap.setOnCameraMoveListener {
            val latLng = mMap.cameraPosition.target
            mapViewModel.onMapMoved(mMap.cameraPosition.zoom, latLng)
        }
    }


}