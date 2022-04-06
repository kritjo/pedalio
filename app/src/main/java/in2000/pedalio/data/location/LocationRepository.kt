package in2000.pedalio.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.data.settings.impl.SharedPreferences

class LocationRepository(val context: Context, val defaultLocation: LatLng, val shouldGetPermission: MutableLiveData<Boolean>) {
    val currentPosition = MutableLiveData(LatLng())
    private val settingsRepository = SharedPreferences(context)

    init {
        (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).let {
            // Check for location permissions
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                // If we don't have permissions, check if we should ask for them
                // This is either if user want to use location or if we don't have asked yet
                if (settingsRepository.gpsToggle || !settingsRepository.askedForGps) {
                    // If we should ask for permissions, request them
                    shouldGetPermission.postValue(true)
                }
                return@let null
            } else {
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
    }

    @SuppressLint("MissingPermission") // should only be called when we have permissions
    fun locationCallback() {
        (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            10,
            1f,
            SimpleLocationListener()
        )
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