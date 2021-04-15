package com.example.piggy.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.piggy.R

class NewCityActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_city)
        val editCityView = findViewById<EditText>(R.id.edit_cty)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editCityView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val city = editCityView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, city)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.piggy.REPLY"
    }
}
