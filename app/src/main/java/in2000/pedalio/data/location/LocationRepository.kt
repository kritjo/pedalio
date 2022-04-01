package in2000.pedalio.data.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.SharedPreferences

class LocationRepository(context: Context, defaultLocation: LatLng) {
    val currentPosition = MutableLiveData(LatLng())
    private val settingsRepository = SharedPreferences(context)

    // LocationManager is a system service that provides access to the device's location and location
    // TODO: Figure out why this lint error is crying wolf
    @SuppressLint("MissingPermission") // Not really missing, we're checking for permissions
    val locationManager: LocationManager? =
        (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).let {
            // Check for location permissions
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                    // If we don't have permissions, check if we should ask for them
                    // This is either if user want to use location or if we don't have asked yet
                    if (settingsRepository.gpsToggle || !settingsRepository.askedForGps) {
                        // If we should ask for permissions, request them
                        requestPermissions(
                            context as Activity,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION),
                            1
                        )
                    }
                    return@let null
            }
            else {
                if (!settingsRepository.gpsToggle) {
                    currentPosition.postValue(defaultLocation)
                    // If we don't want to use location, return null
                    return@let null
                }
                // Register location listener
                it.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10,
                    1f,
                    SimpleLocationListener()
                )
            }
            return@let it
        }

    inner class SimpleLocationListener: android.location.LocationListener {
        override fun onLocationChanged(location: android.location.Location) {
            location.let {
                currentPosition.postValue(LatLng(it.latitude, it.longitude))
            }
        }

        // These three methods has to be overridden, see https://stackoverflow.com/a/64643361
        override fun onStatusChanged(provider: String?, status: Int, extras: android.os.Bundle?) {
            // DO NOT REMOVE
        }

        override fun onProviderEnabled(provider: String) {
            // DO NOT REMOVE
        }

        override fun onProviderDisabled(provider: String) {
            // DO NOT REMOVE
        }
    }
}