package com.example.piggy

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.piggy.database.City
import com.example.piggy.database.CityDao
import kotlinx.coroutines.flow.Flow

class CityRepository(private val cityDao: CityDao) {

    var allCities: Flow<List<City>> = cityDao.getAllData()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(city: City) {
        cityDao.insert(city)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(city: City) {
        cityDao.delete(city.title)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteSingleRow(city: City) {
        cityDao.deleteSingleRow(city)
    }

    fun getAggregatedData(city: String): LiveData<List<City>> {
        return cityDao.getAgregatedData(city)
    }
}
