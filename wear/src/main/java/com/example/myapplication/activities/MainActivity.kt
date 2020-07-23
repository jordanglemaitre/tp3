package com.example.myapplication.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {


    private var mNotificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        if (!hasGps()) {
            Log.d("TAG", "This hardware doesn't have GPS.")
        } else{
            Log.d("TAG", "This hardware have GPS")
        }

        _memoButton.setOnClickListener{
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
        _titleMemo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        _savePlace.setOnClickListener{
            Log.d("Place", "Place 1")
            EnvoieNotificationSimple("JordanleBG")
        }
    }
    private fun hasGps(): Boolean =
        packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)

    fun EnvoieNotificationSimple(title: String?) {
        //Création de la notification
        val mBuilder =
            NotificationCompat.Builder(this.applicationContext, "notify_001")
        //ajout icone, titre et contenu à la notification
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
        mBuilder.setContentTitle("Nouvelle notification")
        mBuilder.setContentText(title)
        //Envoie de la notification sur le téléphone
        mNotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "001"
        val channel =
            NotificationChannel(channelId, "Channel", NotificationManager.IMPORTANCE_HIGH)
        mNotificationManager!!.createNotificationChannel(channel)
        mBuilder.setChannelId(channelId)
        //Utilisation de la méthode notify sur l'objet NotificationManager
        mNotificationManager!!.notify(0, mBuilder.build())
    }
}
