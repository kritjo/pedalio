package in2000.pedalio.data

class Endpoints {
    companion object {
        // Weather API endpoints
        const val NOWCAST_COMPLETE =
            "https://in2000-apiproxy.ifi.uio.no/weatherapi/nowcast/2.0/complete"
        const val LOCATIONFORECAST_COMPLETE =
            "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/complete"

        // Air quality API endpoints
        const val AIRQUALITY_FORECAST =
            "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/"
        const val NILU_FORECAST = "https://api.nilu.no/aq/utd"

        // Bike route API endpoints
        const val OSLO_BIKE_ROUTES =
            "https://geoserver.data.oslo.systems/geoserver/bym/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=bym%3Abyruter&outputFormat=application/json"
    }
}