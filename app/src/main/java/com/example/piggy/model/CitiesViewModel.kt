package com.example.piggy.model

import androidx.lifecycle.*
import com.example.piggy.CityRepository
import com.example.piggy.database.City
import kotlinx.coroutines.launch

class CitiesViewModel(private val repository: CityRepository) : ViewModel() {

    val allCities: LiveData<List<City>> = repository.allCities.asLiveData()

    fun insert(city: City) = viewModelScope.launch {
        repository.insert(city)
    }

    fun delete(city: City) = viewModelScope.launch {
        repository.delete(city)
    }

    fun deleteSingleRow(city: City) = viewModelScope.launch {
        repository.deleteSingleRow(city)
    }

    fun getAggregateData(city: String): LiveData<List<City>> {
        return repository.getAggregatedData(city)
    }
}

class CityViewModelFactory(private val repository: CityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CitiesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
