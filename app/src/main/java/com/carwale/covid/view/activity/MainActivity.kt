package com.carwale.covid.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.carwale.covid.AppApplication

import com.carwale.covid.utils.SharedPreferenceUtil
import com.carwale.covid.view.fragment.CovidMainScreenFragment
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import com.pjtech.covid.R


private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED =
    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    companion object {

        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        var latitude: Double? = null
        var longitude: Double? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }


    private fun requestPermission() {
        requestPermissions(
            PERMISSIONS_REQUIRED,
            PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (hasPermissions(this)) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        val location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            latitude = location.latitude
                            longitude = location.longitude
                            Log.e("Latitude", "" + latitude)
                            getCountryCode()
                            fetchSummary()
                        }
                    } else {
                        Log.w(TAG, "getLastLocation:exception", task.exception)
                        Toast.makeText(
                            this,
                            getString(R.string.no_location_detected),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun getCountryCode() {
        val geocoder = Geocoder(AppApplication.appContext, Locale.getDefault())
        if (latitude != null && longitude != null) {
            val addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1);
            val countryCode = addresses[0].countryCode
            SharedPreferenceUtil(this).setString("countryCode", countryCode)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        // mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            Log.e("Latitude", "" + latitude)
            getCountryCode()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true

    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.navigation_refresh -> {

            try {
                if (hasPermissions(this)) {

                    fetchSummary()
                } else {
                    getLastLocation()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun fetchSummary() {
        val currentFragment =
            nav_host_fragment?.childFragmentManager?.primaryNavigationFragment as CovidMainScreenFragment
        currentFragment.fetchSummary()
    }
}
