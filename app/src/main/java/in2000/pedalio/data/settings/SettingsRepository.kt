package in2000.pedalio.data.settings

abstract class SettingsRepository {
    abstract var distanceUnit: String?
    abstract var theme: Boolean
    abstract var colorBlindMode: Boolean
    abstract var gpsToggle: Boolean
    abstract var askedForGps: Boolean
    abstract var layerAirQuality : Boolean
    abstract var layerWeather : Boolean
}