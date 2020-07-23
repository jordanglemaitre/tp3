package com.example.myapplication.models

import android.content.Context
import android.location.Address
import android.location.Geocoder
import java.util.*

data class Place(val id :Integer,val lon:String,val lat:String,val idUser: Int, var adresse: String){

    fun getAddress(context: Context) {
        var geocoder = Geocoder(context, Locale.getDefault())
        var list : List<Address> = geocoder.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
        this.adresse = list[0].getAddressLine(0)
    }
}
