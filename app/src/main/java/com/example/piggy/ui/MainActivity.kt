package com.example.piggy.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.piggy.CitiesApplication
import com.example.piggy.R
import com.example.piggy.database.City
import com.example.piggy.databinding.ActivityMainBinding
import com.example.piggy.model.CitiesViewModel
import com.example.piggy.model.CityViewModelFactory
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import androidx.lifecycle.observe

class MainActivity : AppCompatActivity() {

    private val newCityActivityRequestCode = 1
    private val viewModel: CitiesViewModel by viewModels {
        CityViewModelFactory((application as CitiesApplication).repository)
    }
    private  lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = MainAdapter(ContentsListener(clickListener = {
            startNewCases(it)
        }, deleteListener = {
            deleteSingleCity(it)

        }))

        binding.recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        viewModel.allCities.observe(owner = this) { cities ->
            cities.let {
                adapter.setContentsList(it)
                binding.recyclerview.adapter = adapter
            }
        }
        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewCityActivity::class.java)
            startActivityForResult(intent, newCityActivityRequestCode)
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
        val alertDialog = AlertDialog.Builder(this@MainActivity).create()
        alertDialog.setTitle(R.string.alert_dialog_title)
        alertDialog.setMessage(getString(R.string.alert_dialog_message))
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK"
        ) { dialog, _ -> dialog.dismiss()

            viewModel.delete(city)
        }
        alertDialog.show()
    }

    private fun startNewCases(value: String) {
        val intent = Intent(this@MainActivity, HistoryActivity::class.java)
        intent.putExtra("singleCity", value)
        startActivity(intent)
    }
}
