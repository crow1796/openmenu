package com.openmenu.traqer.openmenu

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.mapfit.android.Mapfit
import com.mapfit.android.MapfitMap
import com.mapfit.android.OnMapReadyCallback
import com.mapfit.android.annotations.MarkerOptions
import com.mapfit.android.geometry.LatLng
import com.mapfit.android.location.LocationListener
import com.mapfit.android.location.LocationPriority
import com.mapfit.android.location.ProviderStatus
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {

    var LOCATION_PERMISSION_REQUEST_CODE = 1
    var mMapfitMap: MapfitMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission
                        .ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission
                    .ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        Mapfit.getInstance(this, "591dccc4e499ca0001a4c6a4d789733e2d4b4838a1be12fbcdf8d26e")

        setContentView(R.layout.activity_map)

        mapView.getMapAsync(onMapReadyCallback = object: OnMapReadyCallback {
            override fun onMapReady(mapfitMap: MapfitMap){
                mMapfitMap = mapfitMap
                mapfitMap.getMapOptions().setUserLocationEnabled(
                        enable = true,
                        locationPriority = LocationPriority.HIGH_ACCURACY,
                        listener = object: LocationListener {
                            override fun onLocation(location: Location) {
                                val position: LatLng = LatLng(location.latitude, location.longitude)
                                mapfitMap.addMarker(MarkerOptions().position(position))
                            }

                            override fun onProviderStatus(status: ProviderStatus) {

                            }

                        }
                )
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if(permissions.size == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mMapfitMap?.getMapOptions()?.setUserLocationEnabled(true, LocationPriority
                        .HIGH_ACCURACY)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
