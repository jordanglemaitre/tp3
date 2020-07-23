package com.example.myapplication.activities

import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.wear.widget.WearableLinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.api.API
import com.example.myapplication.api.RetroFitHolder
import com.example.myapplication.api.RetrofitInstance
import com.example.myapplication.models.Place
import com.example.myapplication.models.User
import com.google.gson.JsonObject
import com.spotto.android.adapters.PlaceAdapter
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

// PoloEtJojo
// admin
// id 23


class ListActivity : WearableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        _titleMemoListActivity.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        var places : List<Place> = emptyList()
        var that = this

        var placeService: API = RetroFitHolder.retrofit.create(API::class.java)
        placeService.getPlaces(1).also {
            GlobalScope.launch {
                Log.d("Places", it.toString())
                places = it.await()
                for (place in places){
                    place.getAddress(that)
                }
                withContext(Dispatchers.Main){
                    places_recycler_view.apply {
                        this.layoutManager = LinearLayoutManager(context)
                        this.adapter =
                            PlaceAdapter(that, places)
                    }
                }
            }
        }
    }

}