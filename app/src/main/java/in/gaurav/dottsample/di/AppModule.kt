package `in`.gaurav.dottsample.di

import `in`.gaurav.dottsample.dal.LocalDataSource
import `in`.gaurav.dottsample.dal.NetworkDataSource
import `in`.gaurav.dottsample.dal.Repository
import `in`.gaurav.dottsample.model.Venue
import `in`.gaurav.dottsample.network.FourSquareAPI
import `in`.gaurav.dottsample.util.BASE_URL
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


// TODO: separate modules for network, context and picasso

@Module
class AppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideRepo(
        localDataSource: LocalDataSource,
        networkDataSource: NetworkDataSource
    ): Repository {
        return Repository(localDataSource, networkDataSource)
    }

    @Provides
    fun provideNetworkDataSource(retrofit: Retrofit): NetworkDataSource {
        return NetworkDataSource(retrofit.create(FourSquareAPI::class.java))
    }


    @Provides
    fun provideLocalDataSource(retrofit: Retrofit): LocalDataSource {
        return LocalDataSource(MutableLiveData<List<Venue>>(emptyList()))
    }


    @Provides
    fun provideContext(): Context {
        return context
    }


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, converter: GsonConverterFactory): Retrofit {
        return Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(converter)
            .build()
    }


    @Provides
    fun provideOkhttp(interceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {

        return OkHttpClient.Builder().addInterceptor(interceptor).cache(cache).build()
    }

    @Provides
    fun cache(cacheFile: File): Cache {
        return Cache(cacheFile, 12 * 1000 * 1000) //12 MB, no logic
    }

    @Provides
    fun file(context: Context): File {
        val file = File(context.cacheDir, "DiskCache")
        file.mkdirs()
        return file
    }

    @Provides
    fun httpLoggingInterceptor(logger: HttpLoggingInterceptor.Logger): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(logger)
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideLogger(): HttpLoggingInterceptor.Logger {
        return object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("NetworkModule", message)
            }
        }
    }

    @Provides
    fun converter(): GsonConverterFactory {
        val gson = GsonBuilder().create()
        return GsonConverterFactory.create(gson)
    }

}