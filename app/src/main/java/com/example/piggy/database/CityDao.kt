package com.example.piggy.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT id, title, SUM(counter) as counter, create_date FROM city_table GROUP BY title ORDER BY counter")
    fun getAllData(): Flow<List<City>>

    @Query("SELECT id, title, strftime('%d/%m/%Y', create_date/1000,'unixepoch') as create_date, create_date,  SUM(counter) as counter FROM city_table WHERE title =:titleName GROUP BY strftime('%d/%m/%Y',create_date/1000,'unixepoch')")
    fun getAgregatedData(titleName:String): LiveData<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City)

    @Query("DELETE FROM city_table")
    suspend fun deleteAll()

    @Delete()
    suspend fun deleteSingleRow(city: City): Int

    @Query("DELETE  FROM city_table WHERE title =:titleName")
    suspend fun delete(titleName:String): Int
}
