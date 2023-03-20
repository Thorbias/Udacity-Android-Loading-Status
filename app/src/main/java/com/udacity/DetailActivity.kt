package com.udacity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import timber.log.Timber

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val test = intent.getStringExtra("TEST")
        val downloadId = intent.extras?.getLong(DOWNLOAD_ID)

        Timber.d("Received Download ID: %d", downloadId)
        Timber.d("Received TEST %s", test)

        backButton.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

    }

}
