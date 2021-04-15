package com.example.piggy.database

import android.animation.ArgbEvaluator
import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "city_table")

data class City(
        @PrimaryKey @ColumnInfo(name = "id") val id: Int?,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "counter") val counter: Int,
        @ColumnInfo(name = "create_date") val create_date: Long
) {
    fun isPrimeNumber(counter: Int): Boolean {
            var isPrime = true
            for (p in 2 until counter) {
                    if (counter % p === 0) {
                            isPrime = false
                            break
                    }
            }
            return isPrime && counter > 1000
    }

    fun getProperColor(percent: Int, summary:Int): Int {
        val percentage = percent.toFloat() / summary.toFloat()
        return ArgbEvaluator().evaluate(percentage, Color.parseColor("#FFFFFFFF"), Color.parseColor("#FFFFB4B4")) as Int
    }

    fun formatDateTime(value:Long):String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = formatter.format(value)
        return date.toString()
    }
}