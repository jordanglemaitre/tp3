package com.example.myapplication.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : WearableActivity() {

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
        }
    }
    private fun hasGps(): Boolean =
        packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
}
