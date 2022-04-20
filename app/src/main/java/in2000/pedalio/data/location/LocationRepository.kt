package in2000.pedalio.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.location.LocationUpdateListener
import in2000.pedalio.data.settings.impl.SharedPreferences

/**
 * Repository for location data. This class uses the registerListener in order to aquire the location
 * data.
 *
 * @property context
 * @property defaultLocation the default location to use if no location is available
 * @property shouldGetPermission whether the permission to access the location should be requested
 * @property registerListener a function to register a location listener
 */
class LocationRepository(val context: Context,
                         private val defaultLocation: LatLng,
                         private val shouldGetPermission: MutableLiveData<Boolean>,
                         private val registerListener: (input: LocationUpdateListener) -> Unit) {
    /**
     * The location data. Public interface of the location data.
     */
    val currentPosition = MutableLiveData(defaultLocation)
    private val settingsRepository = SharedPreferences(context)

    init {
        (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).let { locationManager ->
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
                registerListener {
                    currentPosition.postValue(LatLng(it.latitude, it.longitude))
                }
            }
            return@let locationManager
        }
    }

    @SuppressLint("MissingPermission") // should only be called when we have permissions
    fun locationCallback(registerListener: (input: LocationUpdateListener) -> Unit) {
        registerListener {
            currentPosition.postValue(LatLng(it.latitude, it.longitude))
        }
    }
}