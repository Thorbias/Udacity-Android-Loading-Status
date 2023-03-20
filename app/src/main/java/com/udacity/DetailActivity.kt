package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import timber.log.Timber

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        )?.cancelNotification()

        val test = intent.getStringExtra("TEST")
        val downloadId = intent.extras?.getLong(DOWNLOAD_ID)

        Timber.d("Received Download ID: %d", downloadId)
        Timber.d("Received TEST %s", test)

        if(downloadId != null) {
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
            if (cursor.moveToFirst()) {
                val idx = cursor.getColumnIndex(DownloadManager.COLUMN_URI)
                val uri = cursor.getString(idx)
                Timber.d("uri of download %s", uri)
                textViewDownload.setText(uri)
            }
        }

        backButton.setOnClickListener{
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

    }

}
