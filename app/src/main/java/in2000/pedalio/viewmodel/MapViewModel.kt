package in2000.pedalio.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tomtom.online.sdk.common.location.LatLng
import in2000.pedalio.R
import in2000.pedalio.data.Endpoints
import in2000.pedalio.data.bikeRoutes.impl.OsloBikeRouteRepostiory
import in2000.pedalio.data.location.LocationRepository
import in2000.pedalio.data.search.SearchResult
import in2000.pedalio.data.weather.impl.LocationforecastRepository
import in2000.pedalio.data.weather.impl.NowcastRepository
import in2000.pedalio.domain.weather.DeviationTypes
import in2000.pedalio.domain.weather.GetDeviatingWeather
import in2000.pedalio.domain.weather.GetWeatherUseCase
import in2000.pedalio.domain.weather.WeatherDataPoint
import in2000.pedalio.ui.map.OverlayBubble
import in2000.pedalio.ui.settings.SettingsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MapViewModel(application: Application) : AndroidViewModel(application) {

    // Pair of LatLng and Color
    val polyline = MutableLiveData(listOf(Pair(listOf(LatLng()), 0)))

    var overlayBubbles = MutableLiveData(mutableListOf<OverlayBubble>())

    val weather = MutableLiveData(WeatherDataPoint(LatLng(),0.0,0.0,0.0, 0.0, 0.0, null))

    val bikeRoutes = MutableLiveData(listOf<List<LatLng>>()).also {
        viewModelScope.launch {
            it.postValue(OsloBikeRouteRepostiory(Endpoints.OSLO_BIKE_ROUTES).getRoutes())
        }
    }

    val shouldGetPermission = MutableLiveData(false)
    private val locationRepository by lazy {
        LocationRepository(application.applicationContext, LatLng(0.0, 0.0), shouldGetPermission)
    }
    val currentPos = locationRepository.currentPosition
    fun permissionCallback() {
        locationRepository.locationCallback()
    }

    val chosenSearchResult = MutableLiveData<SearchResult>()

    private var zoomDensityScaler = 3.0f

    // This is to showcase functionality, should rather use domain layer and repositories
    init {
        // Update weather every 60 seconds
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ viewModelScope.launch(Dispatchers.IO) {
                updateWeatherAndDeviations(application.applicationContext) } }, 60000)
    }

    suspend fun updateWeatherAndDeviations(context: Context) {
        val lat: Double = currentPos.value?.latitude ?: 0.0
        val lng: Double = currentPos.value?.longitude ?: 0.0

        if (lat == 0.0 || lng == 0.0) { return }

        val weatherUseCase =
            GetWeatherUseCase(NowcastRepository(Endpoints.NOWCAST_COMPLETE),
                LocationforecastRepository(Endpoints.LOCATIONFORECAST_COMPLETE))
        val deviatingWeather = GetDeviatingWeather(weatherUseCase, 1.0, 1.0, 0.5,
            listOf(
                LatLng(59.961731, 10.750947), // Korsvoll
                LatLng(59.962913, 10.783847), // Kjellsås
                LatLng(59.941240, 10.81926),  // Bjerke
                LatLng(59.933194,10.670373),  // Huseby
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
            context)
            val weatherData = weatherUseCase.getWeather(currentPos.value!!, context = context)
            weather.postValue(weatherData)
            val deviatingWeatherPoints = deviatingWeather.deviatingPoints(weatherData)
            val bubbles = mutableListOf<OverlayBubble>()
            deviatingWeatherPoints.forEach {
                when (it.deviation) {
                    DeviationTypes.TEMPERATURE -> {
                        val color = if (it.weatherDataPoint.temperature!! > weatherData.temperature!!) {
                            R.color.red
                        } else {
                            R.color.purple_700
                        }
                        bubbles.add(OverlayBubble(it.pos, String.format("%.1f", it.weatherDataPoint.temperature) + "°",
                            context.resources.getColor(color),
                            context.resources.getColor(R.color.off_white)))
                    }
                    DeviationTypes.PERCIPITATION -> {
                        bubbles.add(OverlayBubble(it.pos, String.format("%.1f", it.weatherDataPoint.percipitation) +
                                "\uD83D\uDCA7", // Water drop
                            context.resources.getColor(R.color.black),
                            context.resources.getColor(R.color.off_white)))
                    }
                    DeviationTypes.WIND -> {
                        // Currently Ignored TODO maybe
                    }
                }
            }
            overlayBubbles.postValue(bubbles)
    }

    fun getBubbleSquareSize(context: Context): Int {
        // Get screen size
        val density = context.resources.displayMetrics.densityDpi
        return (density / zoomDensityScaler).roundToInt()
    }

    fun updateBubbleZoomLevel(oldZoomLevel: Double, newZoomLevel: Double): Boolean {
        if (oldZoomLevel.toInt() != newZoomLevel.toInt()) {
            zoomDensityScaler = when (newZoomLevel.toInt()) {
                // TODO: Design people decide scale
                in 0..3 -> 10f
                in 4..6 -> 5f
                in 7..10 -> 4f
                else -> 3f
            }
            return true
        }
        return false
    }
}