package com.example.piggy

import android.app.Application
import com.example.piggy.database.CityRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CitiesApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { CityRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { CityRepository(database.cityDao()) }
}
