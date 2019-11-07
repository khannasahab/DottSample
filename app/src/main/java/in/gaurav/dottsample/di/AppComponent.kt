package `in`.gaurav.dottsample.di

import `in`.gaurav.dottsample.dal.Repository
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getRetrofit(): Retrofit
    fun getRepo(): Repository
}