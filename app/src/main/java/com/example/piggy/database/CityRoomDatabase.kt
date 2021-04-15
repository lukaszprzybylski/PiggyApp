package com.example.piggy.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [City::class], version = 1)
abstract class CityRoomDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var INSTANCE: CityRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CityRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityRoomDatabase::class.java,
                    "cities_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(CityDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class CityDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.cityDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(cityDao: CityDao) {
            cityDao.deleteAll()

            var city = City(null, "WARSAW", 1619, Calendar.getInstance().time.time)
            cityDao.insert(city)
            city = City(null, "LONDON", 60, Calendar.getInstance().time.time)
            cityDao.insert(city)
            city = City(null, "PARIS", 70, Calendar.getInstance().time.time)
            cityDao.insert(city)
            city = City(null, "MADRID", 80, Calendar.getInstance().time.time)
            cityDao.insert(city)
            city = City(null, "PRAGUE", 90,  Calendar.getInstance().time.time)
            cityDao.insert(city)
        }
    }
}
