package in2000.pedalio.viewmodel

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tomtom.online.sdk.common.location.LatLng
import com.tomtom.online.sdk.location.LocationUpdateListener
import in2000.pedalio.R
import in2000.pedalio.data.Endpoints
import in2000.pedalio.data.bikeRoutes.impl.OsloBikeRouteRepostiory
import in2000.pedalio.data.location.LocationRepository
import in2000.pedalio.data.search.SearchResult
import in2000.pedalio.data.weather.impl.LocationForecastRepository
import in2000.pedalio.data.weather.impl.NowcastRepository
import in2000.pedalio.domain.weather.DeviationTypes
import in2000.pedalio.domain.weather.GetDeviatingWeather
import in2000.pedalio.domain.weather.GetWeatherUseCase
import in2000.pedalio.domain.weather.WeatherDataPoint
import in2000.pedalio.ui.map.OverlayBubble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * The view model for the map view.
 * @param application
 */
class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val applicationLocal = application

    /** Lines to be drawn on the map. Pair of lat/long coordinates and a color. */
    val polyline = MutableLiveData(listOf(Pair(listOf(LatLng()), 0)))

    /** overlayBubbles to be drawn on the map. */
    var overlayBubbles = MutableLiveData(mutableListOf<OverlayBubble>())

    /** The current weather data. */
    val weather = MutableLiveData(WeatherDataPoint(LatLng(), 0.0, 0.0, 0.0, 0.0, 0.0, null))

    // The current user location. Could be a fake location, if the user has not granted
    // location permission. Use currentPos() to get the current position.
    private var currentLocation: MutableLiveData<LatLng>? = null

    // Time since weather data was last updated. Only update every 10 seconds.
    private var lastUpdated = 0L

    /** Bike routes from api. */
    val bikeRoutes = MutableLiveData(listOf<List<LatLng>>()).also {
        viewModelScope.launch {
            it.postValue(OsloBikeRouteRepostiory(Endpoints.OSLO_BIKE_ROUTES).getRoutes())
        }
    }

    /** Ids of the tomtom routes currently displayed on the map. */
    val routesOnDisplay = mutableListOf<Long>()

    // How much the bubbles should be scaled.
    private var bubbleZoomDensityScaler = 3.0f

    // ** Callbacks **
    /** Should we request location permission from the user? */
    val shouldGetPermission = MutableLiveData(false)

    /** The chosen search result from the search window. */
    val chosenSearchResult = MutableLiveData<SearchResult?>()

    /** The chosen route from the route selection overlay. */
    val chosenRoute = MutableLiveData<List<LatLng>>()

    /** Callback from the view that we have gotten the permission to access the user's location. */
    fun permissionCallback() {
        locationRepository().locationCallback(registerListener)
    }

    /** The listener that should be used to register for location updates. */
    var registerListener: (input: LocationUpdateListener) -> Unit =
        fun(_: LocationUpdateListener) {}

    // Assure that only one locationRepository is created.
    private var locationRepository: LocationRepository? = null

    // The repository that should be used to get the current location, using the registerListener.
    private fun locationRepository(): LocationRepository {
        if (locationRepository == null) {
            locationRepository = LocationRepository(
                applicationLocal.applicationContext,
                LatLng(59.92, 10.75),
                shouldGetPermission,
                registerListener
            )
        }
        return locationRepository!!
    }

    /**
     * @return Is the current location the default location?
     */
    fun currentLocationIsDefault(): Boolean {
        return locationRepository().currentPositionIsDefault
    }

    // Ensure that only one copy of the currentLocation LiveData is created.
    /**
     * @return The current location of the user as a LiveData, updated every time the user moves.
     */
    fun currentPos(): MutableLiveData<LatLng> {
        if (currentLocation == null) {
            currentLocation = locationRepository().currentPosition
        }
        return currentLocation as MutableLiveData<LatLng>
    }

    /**
     * Update the current weather data and the overlay bubbles.
     * @param context
     */
    suspend fun updateWeatherAndDeviations(context: Context) {
        val lat: Double = currentPos().value?.latitude ?: 0.0
        val lng: Double = currentPos().value?.longitude ?: 0.0

        // If fake location, don't update weather.
        if (lat == 0.0 || lng == 0.0) {
            return
        }

        // Only update every 10 seconds.
        val currMill = System.currentTimeMillis()
        if (currMill - lastUpdated < 10000 && lastUpdated != 0L) {
            return
        }
        lastUpdated = currMill

        val weatherUseCase =
            GetWeatherUseCase(
                NowcastRepository(Endpoints.NOWCAST_COMPLETE),
                LocationForecastRepository(Endpoints.LOCATIONFORECAST_COMPLETE)
            )

        // Get the deviations from the weather to be used in the overlay bubbles.
        val deviatingWeather = GetDeviatingWeather(
            weatherUseCase, 1.0, 1.0, 0.5,
            listOf(
                LatLng(59.961731, 10.750947), // Korsvoll
                LatLng(59.962913, 10.783847), // Kjellsås
                LatLng(59.941240, 10.81926),  // Bjerke
                LatLng(59.933194, 10.670373),  // Huseby
                LatLng(59.922826, 10.679366), // Skøyen
                LatLng(59.930228, 10.862871), // Alna
                LatLng(59.942360, 10.704445), // Vindern
                LatLng(59.940463, 10.723815), // Blindern
                LatLng(59.926933, 10.716704), // Majorstuen
                LatLng(59.916738, 10.706849), // Frogner
                LatLng(59.937963, 10.736285), // Ullevål
                LatLng(59.928753, 10.741683), // St. Hanshaugen
                LatLng(59.912863, 10.732636), // Sentrum
                LatLng(59.945751, 10.780016), // Storo
                LatLng(59.934095, 10.784416), // Sinsen
                LatLng(59.933764, 10.764026), // Torshov
                LatLng(59.926330, 10.777925), // Carl Berners Plass
                LatLng(59.921870, 10.758257), // Grunerløkka
                LatLng(59.915281, 10.768540), // Tøyen
                LatLng(59.915834, 10.804612), // Helsfyr
            ),
            context
        )

        // Get the weather data from the current location.
        val weatherData = weatherUseCase.getWeather(currentPos().value!!, context = context)
        weather.postValue(weatherData)

        // Get the weather deviations compared to the current location.
        val deviatingWeatherPoints = deviatingWeather.deviatingPoints(weatherData)

        // Generate the overlay bubbles.
        val bubbles = mutableListOf<OverlayBubble>()
        deviatingWeatherPoints.forEach {
            when (it.deviation) {
                DeviationTypes.TEMPERATURE -> {
                    val color = if (it.weatherDataPoint.temperature!! > weatherData.temperature!!) {
                        R.color.red
                    } else {
                        R.color.purple_700
                    }
                    bubbles.add(
                        OverlayBubble(
                            it.pos, String.format("%.1f", it.weatherDataPoint.temperature) + "°",
                            context.resources.getColor(color, applicationLocal.theme),
                            context.resources.getColor(R.color.off_white, applicationLocal.theme)
                        )
                    )
                }
                DeviationTypes.PRECIPITATION -> {
                    bubbles.add(
                        OverlayBubble(
                            it.pos, String.format("%.1f", it.weatherDataPoint.precipitation) +
                                    "\uD83D\uDCA7", // Water drop
                            context.resources.getColor(R.color.black, applicationLocal.theme),
                            context.resources.getColor(R.color.off_white, applicationLocal.theme)
                        )
                    )
                }
                DeviationTypes.WIND -> {
                    // Currently Ignored. Design choice.
                }
            }
        }
        overlayBubbles.postValue(bubbles)
    }

    /**
     * Responsive size calculation of overlay bubbles
     *
     * @param context
     * @return Size of overlay bubbles given screen size and zoom level
     */
    fun getBubbleSquareSize(context: Context): Int {
        // Get screen size
        val density = context.resources.displayMetrics.densityDpi
        return (density / bubbleZoomDensityScaler).roundToInt()
    }

    /**
     * @param oldZoomLevel The old zoom level of the map
     * @param newZoomLevel The new zoom level of the map
     * @return Whether we should update the bubbles size
     */
    fun updateBubbleZoomLevel(oldZoomLevel: Double, newZoomLevel: Double): Boolean {
        if (oldZoomLevel.toInt() != newZoomLevel.toInt()) {
            bubbleZoomDensityScaler = when (newZoomLevel.toInt()) {
                // Scaling
                in 0..3 -> 10f
                in 4..6 -> 5f
                in 7..10 -> 4f
                else -> 3f
            }
            return true
        }
        return false
    }

    init {
        // Update weather every 60 seconds even if the user has not moved.
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelScope.launch(Dispatchers.IO) {
                updateWeatherAndDeviations(application.applicationContext)
            }
        }, 60000)
    }
}