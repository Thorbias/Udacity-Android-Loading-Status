package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.createChannel(
            CHANNEL_ID,
            getString(R.string.channel_name)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            Timber.d("Button Selected: %s", downloadRadioGroup.checkedRadioButtonId)

            when (downloadRadioGroup.checkedRadioButtonId) {
                R.id.download1RadioBtn -> {
                    Timber.d("Button1")
                    download(URL1)
                }
                R.id.download2RadioBtn -> {
                    Timber.d("Button2")
                    download(URL2)
                }
                R.id.download3RadioBtn -> {
                    Timber.d("Button3")
                    download(URL3)
                }
                else -> {
                    Timber.d("Nothing selected")
                    Toast.makeText(
                        applicationContext,
                        R.string.make_a_selection,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Timber.d("broadcast received %d", id)
            if (id == downloadID) {
                var message = "Download Complete!"
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor = downloadManager.query(DownloadManager.Query().setFilterById(id))
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(DownloadManager.COLUMN_URI)
                    val uri = cursor.getString(idx)
                    Timber.d("uri of download %s", uri)
                    message = getString(R.string.notification_description, uri)
                }
                Toast.makeText(applicationContext, R.string.download_complete, Toast.LENGTH_SHORT)
                    .show()
                Timber.d("Calling sendNotification with downloadId: %d", id)

                notificationManager.sendNotification(message, id, CHANNEL_ID, applicationContext)

            }
        }
    }

    private fun download(url: String) {
        Timber.d("downloading URL: %s", url)
        notificationManager.cancelNotification()
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL1 =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL2 =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL3 =
            "https://github.com/square/retrofit/master.zip"
        private const val CHANNEL_ID = "downloadChannelId"
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}
