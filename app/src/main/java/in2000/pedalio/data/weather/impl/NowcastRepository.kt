package in2000.pedalio.data.weather.impl

import in2000.pedalio.data.weather.WeatherRepository

class NowcastRepository(lat: Double, lon: Double) : WeatherRepository(lat, lon) {
    override fun getTemp(timeDelta: Int): Double {
        TODO("Not yet implemented")
    }

    override fun getPercipitationRate(timeDelta: Int): Double {
        TODO("Not yet implemented")
    }

    override fun getPercipitation(timeDelta: Int): Double {
        TODO("Not yet implemented")
    }

    override fun getRelativeHumidity(timeDelta: Int): Double {
        TODO("Not yet implemented")
    }

    override fun getWindDirection(timeDelta: Int): Double {
        TODO("Not yet implemented")
    }

    override fun getWindSpeed(timeDelta: Int): Double {
        TODO("Not yet implemented")
    }

    override fun getGustSpeed(timeDelta: Int): Double {
        TODO("Not yet implemented")
    }

    override fun radarCoverage(): Boolean {
        TODO("Not yet implemented")
    }

}