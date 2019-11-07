package `in`.gaurav.dottsample.ui

import `in`.gaurav.dottsample.dal.LocalDataSource
import `in`.gaurav.dottsample.dal.NetworkDataSource
import `in`.gaurav.dottsample.dal.Repository
import `in`.gaurav.dottsample.model.Location
import `in`.gaurav.dottsample.util.DistanceUtility
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


/**
 * Note: This class showcases the tests, we need to write more for all the components
 */

@ExtendWith(InstantExecutorExtension::class)
internal class MapViewModelTest {


    @Test
    fun locationThresholdShouldFail_WhenMapZoomIsLessThanThreshold() {
        val viewModel = MapViewModel()
        assertFalse(viewModel.isNewLocationUnderFetchThreshold(1.0f, LatLng(0.0, 0.0)))
    }

    @Test
    fun locationThresholdShouldPass_WhenLastLocationIsNew() {
        val viewModel = MapViewModel()
        assertTrue(viewModel.isNewLocationUnderFetchThreshold(10.0f, LatLng(0.0, 0.0)))
    }


    @Test
    fun ensureDistanceUtilityIsCalled() {
        val viewModel = MapViewModel()
        viewModel.lastFetchedLocation = Location(70.12, 32.43)
        viewModel.distanceUtility = mock(DistanceUtility::class.java)
        val latLng = LatLng(79.12, 42.12)
        viewModel.isNewLocationUnderFetchThreshold(10.0f, latLng)
        verify(viewModel.distanceUtility).getDistancebetween(
            latLng,
            viewModel.lastFetchedLocation
        )
    }

    @Test
    fun ensureRepositoryLoadPlacesIsCalled() {
        runBlocking {
            val viewModel = MapViewModel()
            viewModel.repository = mock(Repository::class.java)
            val location = Location(70.12, 32.43)
            viewModel.fetchNearPlaces(location)
            verify(viewModel.repository).loadNearByPlaces(location)
        }
    }

    @Test
    fun ensureNetworkDataSourceIsCalled() {
        runBlocking {
            val viewModel = MapViewModel()
            val networkDataSource = mock(NetworkDataSource::class.java)
            val localDataSource = mock(LocalDataSource::class.java)
            val repository = Repository(localDataSource, networkDataSource)
            viewModel.repository = repository
            val location = Location(70.12, 32.43)
            viewModel.fetchNearPlaces(location)
            verify(networkDataSource).loadNearPlaces(location)
        }
    }

}