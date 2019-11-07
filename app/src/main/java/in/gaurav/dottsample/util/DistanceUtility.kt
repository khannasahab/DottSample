package `in`.gaurav.dottsample.util

import `in`.gaurav.dottsample.model.Location
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

class DistanceUtility {

    fun isDistanceUnderThreshold(distanceBetween: Double, mapZoom: Float): Boolean {
        return distanceBetween > getMinDistanceBetweenZoom(mapZoom)
    }

    /**
     * This function is based on hit and try! let's not bombard the server
     */
    private fun getMinDistanceBetweenZoom(mapZoom: Float): Int {
        return when (mapZoom) {
            in 9.0..10.0 -> 4500
            in 10.0..11.0 -> 3500
            in 11.0..12.0 -> 2500
            in 12.0..13.0 -> 1500
            in 13.0..14.0 -> 1000
            in 14.0..15.0 -> 750
            in 15.0..16.0 -> 500
            in 16.0..17.0 -> 350
            else -> 200
        }
    }

    fun getDistancebetween(location: LatLng, lastFetchedLocation: Location): Double {
        return SphericalUtil.computeDistanceBetween(
            LatLng(
                lastFetchedLocation.lat,
                lastFetchedLocation.lng
            ), location
        )
    }
}