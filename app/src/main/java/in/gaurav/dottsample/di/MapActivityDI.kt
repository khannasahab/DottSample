package `in`.gaurav.dottsample.di

import `in`.gaurav.dottsample.ui.MapViewFragment
import `in`.gaurav.dottsample.util.DistanceUtility
import `in`.gaurav.dottsample.ui.MapViewModel
import `in`.gaurav.dottsample.ui.MapsActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

/*
    TODO We could have separate component for Fragments, not using in this example
 */
@Module
class MapActivityModule(val activity: MapsActivity) {

    @Provides
    @MapActivityScope
    fun provideViewModel(): MapViewModel {
        //TODO create own viewmodelprovider to take care of DI
        return ViewModelProviders.of(activity).get(MapViewModel::class.java)
    }

    @Provides
    @MapActivityScope
    fun provideLocationProvider(): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(activity)
    }

    @Provides
    @MapActivityScope
    fun provideDistanceUtility(): DistanceUtility =
        DistanceUtility()
}


@MapActivityScope
@Component(dependencies = [AppComponent::class], modules = [MapActivityModule::class])
interface MapActivityComponent {
    fun inject(activity: MapsActivity)
    fun inject(mapFragment: MapViewFragment)
    fun inject(viewModel: MapViewModel)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MapActivityScope