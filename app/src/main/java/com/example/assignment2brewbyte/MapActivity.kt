package com.example.assignment2brewbyte

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.assignment2brewbyte.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap

    private val LOCATION_PERMISSION_CODE = 101
    private var isSatellite = false
    private var brewByteMarker: Marker? = null

    // BrewByte at Georgian College
    private val BREWBYTE_LATLNG = LatLng(44.4125, -79.6686)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener { finish() }

        // Load map fragment
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // ===== BOTTOM BUTTONS =====
        binding.btnMyLocation.setOnClickListener {
            enableMyLocation()
        }

        binding.btnMapType.setOnClickListener {
            if (!::mMap.isInitialized) {
                Toast.makeText(this, "Map is still loading...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            isSatellite = !isSatellite
            mMap.mapType =
                if (isSatellite) GoogleMap.MAP_TYPE_SATELLITE else GoogleMap.MAP_TYPE_NORMAL
        }

        binding.btnDirections.setOnClickListener {
            // Open Google Maps navigation to BrewByte
            val gmmIntentUri = Uri.parse("google.navigation:q=${BREWBYTE_LATLNG.latitude},${BREWBYTE_LATLNG.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(this, "Google Maps app not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMockLocation.setOnClickListener {
            if (!::mMap.isInitialized) {
                Toast.makeText(this, "Map is still loading...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Example mock location: inside campus courtyard
            val mockLatLng = LatLng(44.4129, -79.6678)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mockLatLng, 18f))

            // Optional: move marker to mock spot
            brewByteMarker?.remove()
            brewByteMarker = mMap.addMarker(
                MarkerOptions()
                    .position(mockLatLng)
                    .title("Mock BrewByte Location")
            )

            Toast.makeText(this, "Mock location set on map", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add BrewByte marker
        brewByteMarker = mMap.addMarker(
            MarkerOptions()
                .position(BREWBYTE_LATLNG)
                .title("BrewByte Cafe")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BREWBYTE_LATLNG, 16f))

        // Try to show user location (if permission already granted)
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (!::mMap.isInitialized) return

        // Check permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
            return
        }

        // Permission granted → enable blue dot + my-location button on map
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        Toast.makeText(this, "Showing your location on the map", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocation()
        } else if (requestCode == LOCATION_PERMISSION_CODE) {
            Toast.makeText(
                this,
                "Location permission is needed to show your position on the map",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
