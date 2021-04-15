package com.example.piggy.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.piggy.CitiesApplication
import com.example.piggy.R
import com.example.piggy.database.City
import com.example.piggy.databinding.ActivityHistoryBinding
import com.example.piggy.model.CitiesViewModel
import com.example.piggy.model.CityViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class HistoryActivity : AppCompatActivity() {

    private val newCityActivityRequestCode = 1
    private val viewModel: CitiesViewModel by viewModels {
        CityViewModelFactory((application as CitiesApplication).repository)
    }
    private  lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_history)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val city = intent.getStringExtra("singleCity")
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_new_cases)
        val adapter = HistoryAdapter(ContentsNewListener(clickListener = {
            //
        }, deleteListener = {
            deleteSingleCity(it)
        }))

        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(this)

        city?.let { it ->
            viewModel.getAggregateData(it).observe(owner = this) { list ->
                supportActionBar?.title = city + " "+list.sumOf { it.counter }
                adapter.setContentsList(list)
                recyclerView.adapter = adapter
            }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab_new)
        fab.setOnClickListener {
            val cityValue = city?.let { it1 -> City(null, it1, randomInt(), randomDate()) }
            cityValue?.let { it1 -> viewModel.insert(it1) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newCityActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NewCityActivity.EXTRA_REPLY)?.let { reply ->
                val city = City(null, reply, randomInt(), randomDate())
                viewModel.insert(city)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun randomInt():Int {
        val rand = Random()
        return rand.nextInt(10000 - 0 + 1) + 0
    }

    private fun randomDate():Long {
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.DATE, -7)

        val startDate = Calendar.getInstance().time.time
        val random: Long = ThreadLocalRandom.current().nextLong(endDate.time.time ,startDate)
        val date = Date(random)
        return date.time
    }

    private fun deleteSingleCity(city: City) {
        val alertDialog = AlertDialog.Builder(this@HistoryActivity).create()
        alertDialog.setTitle(R.string.alert_dialog_title)
        alertDialog.setMessage(getString(R.string.alert_dialog_message))
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
        ) { dialog, _ ->
            dialog.dismiss()
            viewModel.deleteSingleRow(city)
        }
        alertDialog.show()
    }
}
