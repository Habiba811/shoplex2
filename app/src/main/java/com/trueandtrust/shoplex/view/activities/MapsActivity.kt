package com.trueandtrust.shoplex.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.tasks.Task
import com.trueandtrust.shoplex.R
import com.trueandtrust.shoplex.databinding.ActivityMapsBinding
import com.trueandtrust.shoplex.model.enumurations.LocationAction
import com.trueandtrust.shoplex.model.extra.StoreInfo
import com.trueandtrust.shoplex.model.maps.LocationManager

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object{
        const val MAPS_CODE = 202
        const val LOCATION_ACTION = "LOCATION_ACTION"
        const val ADDRESS = "ADDRESS"
        const val LOCATION = "LOCATION"
    }

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private var currentLocation: Location? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val REQUEST_CODE = 101
    private lateinit var locationAction: LocationAction

    override fun onCreate(savedInstanceState: Bundle?) {
        if(StoreInfo.lang != this.resources.configuration.locale.language)
            StoreInfo.setLocale(StoreInfo.lang, this)
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestPermission()
        if(intent.getStringExtra(LOCATION_ACTION) == LocationAction.Add.name){
            locationAction = LocationAction.Add
        }

        binding.btnOK.setOnClickListener {
            when(locationAction){
                LocationAction.Add -> {
                    setResult(RESULT_OK, Intent().apply {
                        val selectedLocation = com.trueandtrust.shoplex.model.pojo.Location(locationManager.selectedLocation.latitude, locationManager.selectedLocation.longitude)
                        val address = locationManager.getAddress(
                            com.trueandtrust.shoplex.model.pojo.Location(
                                selectedLocation.latitude,
                                selectedLocation.longitude
                            ))
                        putExtra(LOCATION, selectedLocation)
                        putExtra(ADDRESS, address)
                    })
                }
            }
            finish()
        }
    }

    private fun requestPermission() {
        if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)

        } else{
            val task: Task<Location> = mFusedLocationClient.lastLocation
            task.addOnSuccessListener { location ->
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
                currentLocation = location
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        locationManager = LocationManager.getInstance(mGoogleMap, this)

        locationManager.addMarker(currentLocation)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestPermission()
                }
                return
            }
        }
    }
}