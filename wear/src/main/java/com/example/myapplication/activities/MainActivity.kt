package com.example.myapplication.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : WearableActivity() , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnSuccessListener<Location?>
    {

    override fun onPause() {
        super.onPause()
    }

    private var flpc: FusedLocationProviderClient? = null
    private var lc: LocationCallback? = null
    private var gpsPresence: TextView? = null
    private var mTextView: TextView? = null
    private var capabilityClient: CapabilityClient? = null
    private val nodes: MutableSet<Node> = HashSet()


    override fun onResume() {
        super.onResume()
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        capabilityClient = Wearable.getCapabilityClient(this)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        _button2.setOnClickListener {
            clickSearchNodes()
        }

         checkPermission()

         setAmbientEnabled()

        val gapi = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        gapi.connect()

        _titleMemo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        _memoButton.setOnClickListener{
            Log.d("Zouz","zoug")
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }

    fun clickSearchNodes() {
        Log.i("CIO", "Searching nodes...")
        val capabilitiesTask = capabilityClient!!.getAllCapabilities(CapabilityClient.FILTER_REACHABLE)
        capabilitiesTask.addOnSuccessListener(
            OnSuccessListener { capabilityInfoMap ->
                nodes.clear()
                if (capabilityInfoMap.isEmpty()) {
                    Log.i("CIO", "No capability advertised :/")
                    return@OnSuccessListener
                }
                for (capabilityName in capabilityInfoMap.keys) {
                    val capabilityInfo = capabilityInfoMap[capabilityName]
                    Log.i("CIO", "Capability found: " + capabilityInfo!!.name)
                    if (capabilityInfo != null) {
                        nodes.addAll(capabilityInfo.nodes)
                    }
                }
                Log.i("CIO", "Nodes found: $nodes")
                val printNodes = StringBuilder()
                for (node in nodes) printNodes.append(node.displayName)
                    .append(" ")
            })
    }

    fun clickSendMessage(lat:String, lon:String) {
        Log.i("CIO", "Sending a message...")
        val clientMessage : MessageClient = Wearable.getMessageClient(this)
        for (node in nodes) {
            Log.i("CIO", "  - to " + node.id)
            val message = "Hello $lat $lon"
            val sendMessageTask : Task<Int> =
                clientMessage.sendMessage(node.id, "CIO", message.toByteArray())
        }
    }

    // Should check if the permission is granted for this app
    @SuppressLint("MissingPermission")
    override fun onConnected(bundle: Bundle?) {
        Log.i("CIO", "GAPI connected")

        // Get the location provider through the GoogleApi
        flpc = LocationServices.getFusedLocationProviderClient(this)
        // Check location availability
        @SuppressLint("MissingPermission") val locAvailable = flpc!!.getLocationAvailability()
        Log.i("CIO", locAvailable.isSuccessful.toString())
        Log.i("CIO", locAvailable.isCanceled.toString())
        val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        println("test co")
        println(gps_enabled)
        println(network_enabled)
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder(this)
                .setMessage("network not found")
                .setPositiveButton("ok") { paramDialogInterface, paramInt -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("annuler", null)
                .show()
        }
        locAvailable.addOnSuccessListener { locationAvailability -> Log.i("CIO", "Location is available = $locationAvailability") }
        locAvailable.addOnFailureListener { e -> Log.i("CIO", "Failure :  " + e.localizedMessage) }
        Log.i("CIO", "avant locrequest")

        // Ask for update of the location
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(1000).setFastestInterval(1000)
        Log.i("CIO", "apres locrequest")
        lc = LocationCallback()
        Log.i("CIO", lc.toString())
        Log.i("CIO", locationRequest.toString())
        flpc!!.requestLocationUpdates(locationRequest, lc, null)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i("CIO", "Error with GAPI connection")
    }

    // Should check if the permission is granted for this app
    @SuppressLint("MissingPermission")
    fun getlocation(view: View?) {
        val loc = flpc!!.lastLocation
        loc.addOnSuccessListener(this)
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { //Can add more as per requirement
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                123)
        }
    }

    // Callback for the location task
    override fun onSuccess(loc: Location?) {
        Log.i("CIO", "affiche loc")
        Log.i("CIO", loc.toString())
        if (loc != null) {
            val df = DecimalFormat("#.##")
            val lon = df.format(loc.longitude)
            val lat = df.format(loc.latitude)
            Log.i("CIO", "Location: $lon , $lat")
            clickSendMessage(lat, lon)
        } else {
            Log.i("CIO", "No defined location ! Are you inside ? Have you authorized location on the smartwatch ?")
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.i("CIO", "GAPI supsended")
    }

    override fun onStop() {
        super.onStop()
        if (lc != null) flpc!!.removeLocationUpdates(lc)
    }
}