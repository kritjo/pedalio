package in2000.pedalio.data.location

import android.content.Context
import android.content.pm.PackageManager
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
 * @constructor [defaultLocation]: the default location to use if no location is available,
 * [shouldGetPermission]: whether the permission to access the location should be requested,
 * [registerListener]: a function to register a location listener
 */
class LocationRepository(
    val context: Context,
    private val defaultLocation: LatLng,
    private val shouldGetPermission: MutableLiveData<Boolean>,
    private val registerListener: (input: LocationUpdateListener) -> Unit
) {
    /**
     * The location data. Public interface of the location data.
     */
    val currentPosition = MutableLiveData(defaultLocation)
    /**
     * Is the current position recorded unchanged? In that case it is a "fake" location and not
     * recorded from the GPS.
     */
    var currentPositionIsDefault = true
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
                    currentPositionIsDefault = false
                }
            }
            return@let locationManager
        }
    }

    /**
     * Register a new location listener.
     */
    fun locationCallback(registerListener: (input: LocationUpdateListener) -> Unit) {
        registerListener {
            currentPosition.postValue(LatLng(it.latitude, it.longitude))
            currentPositionIsDefault = false
        }
    }

    /**
     * Register a new location listener that only cares about non default values.
     */
    fun registerNewListener(registerListener: (input: LocationUpdateListener) -> Unit) {
        registerListener {
            if (it != defaultLocation) {
                currentPosition.postValue(LatLng(it.latitude, it.longitude))
                currentPositionIsDefault = false
            }
        }
    }
}