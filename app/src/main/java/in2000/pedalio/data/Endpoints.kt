package in2000.pedalio.data

class Endpoints {
    companion object {
        const val NOWCAST_COMPLETE = "https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/complete"
        const val AIRQUALITY_FORECAST = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?areaclass=grunnkrets&lat=59.928231&lon=10.778600&show=all"
        const val LOCATIONFORECAST_COMPLETE = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/complete"
    }
}