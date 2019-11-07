package `in`.gaurav.dottsample.ui

import `in`.gaurav.dottsample.DottApplication
import `in`.gaurav.dottsample.R
import `in`.gaurav.dottsample.di.DaggerMapActivityComponent
import `in`.gaurav.dottsample.di.MapActivityComponent
import `in`.gaurav.dottsample.di.MapActivityModule
import `in`.gaurav.dottsample.util.DETAIL_FRAGMENT_TAG
import `in`.gaurav.dottsample.util.MAP_FRAGMENT_TAG
import `in`.gaurav.dottsample.util.PERMISSIONFRAGMENT_TAG
import `in`.gaurav.dottsample.util.REQUEST_CODE_LOCATION_PERMISSION
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import javax.inject.Inject

/**
 * The Activity is AutoGenerated with Android Studio Template
 */
class MapsActivity : AppCompatActivity() {

    lateinit private var activityComponent: MapActivityComponent

    @Inject
    lateinit var mapViewModel: MapViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        initInjection()
        initObserver()
    }

    override fun onStart() {
        super.onStart()
        if (hasPermission()) {
            addMapFragment()
        } else {
            addPermissionFragment()
        }
    }

    private fun addPermissionFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, PermissionFragment.getInstance(), PERMISSIONFRAGMENT_TAG)
            .commit()
    }

    private fun hasPermission(): Boolean =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


    private fun initObserver() {
        mapViewModel.observeLastVenue(this, Observer {
            it?.let {
                if (isFragmentNotAdded(DETAIL_FRAGMENT_TAG) && hasPermission()) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.content, VenueDetailFragment.getInstance(), DETAIL_FRAGMENT_TAG)
                        .addToBackStack(
                            DETAIL_FRAGMENT_TAG
                        ).commit()
                }
            }
        })
    }

    private fun addMapFragment() {
        if (isFragmentNotAdded(MAP_FRAGMENT_TAG)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, MapViewFragment.getInstance(), MAP_FRAGMENT_TAG).commit()
        }
    }

    private fun isFragmentNotAdded(tag: String) =
        supportFragmentManager.findFragmentByTag(tag) == null

    private fun initInjection() {
        activityComponent = DaggerMapActivityComponent.builder()
            .appComponent((applicationContext as DottApplication).appComponent)
            .mapActivityModule(
                MapActivityModule(this)
            ).build()
        activityComponent.inject(this)
        activityComponent.inject(mapViewModel)
    }

    fun daggerComponent(): MapActivityComponent = activityComponent


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (hasPermission()) {
                addMapFragment()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}