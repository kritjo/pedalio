package in2000.pedalio.data.airquality.impl

import in2000.pedalio.data.airquality.AirQualityRepository
import in2000.pedalio.data.airquality.source.AQF.AQFSource
import in2000.pedalio.data.airquality.source.AQF.Time
import in2000.pedalio.utils.DateTime

/**
 * Implementation of AirQuality Repository using AQF from met.no
 */
class AQFRepository(val endpoint: String) : AirQualityRepository() {
    override suspend fun getNOX(lat: Double, lon: Double, timeDelta: Int): Double {
        val timeslots: List<Time> = AQFSource.getForecast(endpoint, lat, lon)
            .data
            .time
        val closestHour = DateTime.closest_hour(
            DateTime.timedelta_milli(
                timeDelta))
        var selectedSlot: Time? = null
        timeslots.forEach { if (it.from == closestHour) selectedSlot = it }
        if (selectedSlot == null) throw IllegalStateException("Could not get a timeslot for nox")

        return  selectedSlot!!.variables.AQI_no2.value
    }

    override suspend fun getPM10(lat: Double, lon: Double, timeDelta: Int): Double {
        val timeslots: List<Time> = AQFSource.getForecast(endpoint, lat, lon)
            .data
            .time
        val closestHour = DateTime.closest_hour(
            DateTime.timedelta_milli(
                timeDelta))
        var selectedSlot: Time? = null
        timeslots.forEach { if (it.from == closestHour) selectedSlot = it }
        if (selectedSlot == null) throw IllegalStateException("Could not get a timeslot for pm10")

        return  selectedSlot!!.variables.pm10_concentration.value
    }

    override suspend fun getPM25(lat: Double, lon: Double, timeDelta: Int): Double {
        val timeslots: List<Time> = AQFSource.getForecast(endpoint, lat, lon)
            .data
            .time
        val closestHour = DateTime.closest_hour(
            DateTime.timedelta_milli(
                timeDelta))
        var selectedSlot: Time? = null
        timeslots.forEach { if (it.from == closestHour) selectedSlot = it }
        if (selectedSlot == null) throw IllegalStateException("Could not get a timeslot for pm25")

        return  selectedSlot!!.variables.pm25_concentration.value
    }

    override suspend fun getAQI(lat: Double, lon: Double, timeDelta: Int): Double {
        val timeslots: List<Time> = AQFSource.getForecast(endpoint, lat, lon)
            .data
            .time
        val closestHour = DateTime.closest_hour(
                DateTime.timedelta_milli(
                    timeDelta))
        var selectedSlot: Time? = null
        timeslots.forEach { if (it.from == closestHour) selectedSlot = it }
        if (selectedSlot == null) throw IllegalStateException("Could not get a timeslot for aqi")

        return  selectedSlot!!.variables.AQI.value
    }
}