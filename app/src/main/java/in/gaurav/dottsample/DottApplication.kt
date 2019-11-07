package `in`.gaurav.dottsample

import `in`.gaurav.dottsample.di.AppComponent
import `in`.gaurav.dottsample.di.AppModule
import `in`.gaurav.dottsample.di.DaggerAppComponent
import android.app.Application

class DottApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}