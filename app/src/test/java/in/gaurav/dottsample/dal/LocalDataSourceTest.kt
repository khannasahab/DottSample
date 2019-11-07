package `in`.gaurav.dottsample.dal

import `in`.gaurav.dottsample.model.Venue
import androidx.lifecycle.MutableLiveData
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

/*
* Note: This class showcases the tests, we need to write more for all the components
 */
internal class LocalDataSourceTest {

    @Test
    fun ensurePostValueIsCalled() {
        val data = mock(MutableLiveData::class.java) as MutableLiveData<List<Venue>>
        val localDataSource = LocalDataSource(data)
        localDataSource.insertAll(emptyList())
        verify(data).postValue(ArgumentMatchers.any())
    }

    @Test
    fun ensureSizeOfLocalCache() {
        val data = mock(MutableLiveData::class.java) as MutableLiveData<List<Venue>>
        val localDataSource = LocalDataSource(data)
        `when`(data.value).thenReturn(listOf(Venue()))
        assert(localDataSource.totalVenues() == 1)
    }

    @Test
    fun ensureLocalCacheIsDeleted() {
        val data = mock(MutableLiveData::class.java) as MutableLiveData<List<Venue>>
        val localDataSource = LocalDataSource(data)
        localDataSource.deleteAll()
        verify(data).postValue(emptyList())
    }

}