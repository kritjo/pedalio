package in2000.pedalio.utils

import android.icu.util.Calendar
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Utility Functions for date and time
 * ISO is ISO 8609, milli is ms since epoch
 */
class DateTime {
    companion object {
        @JvmStatic
        val currentISOTime
            get() = DateTimeFormatter.ISO_INSTANT.format(
                Instant.ofEpochMilli(
                    Calendar.getInstance().timeInMillis
                )
            )

        @JvmStatic
        val currentClosestHour
            get() = closest_hour(Calendar.getInstance().timeInMillis)

        @JvmStatic
        val currentTimeInMillis
            get() = Calendar.getInstance().timeInMillis

        @JvmStatic
        fun iso_to_milli(iso: String): Long {
            val calendar = Calendar.getInstance().apply {
                val split = iso.split("Z")[0].split("-", "T", ":", ".", "+").map { it.toInt() }
                set(Calendar.YEAR, split[0])
                set(Calendar.MONTH, split[1]-1) // January is 0
                set(Calendar.DATE, split[2])
                set(Calendar.HOUR_OF_DAY, split[3])
                set(Calendar.MINUTE, split[4])
                set(Calendar.SECOND, split[5])
                if (iso.contains(".")) {
                    set(Calendar.MILLISECOND, split[6])
                    if (iso.contains("+")) {
                        val gc =  GregorianCalendar(TimeZone.getTimeZone("+" + split[7]))
                        gc.timeInMillis = timeInMillis
                        return gc.timeInMillis
                    }
                } else {
                    set(Calendar.MILLISECOND, 0)
                    if (iso.contains("+")) {
                        val gc =  GregorianCalendar(TimeZone.getTimeZone("+" + split[6]))
                        gc.timeInMillis = timeInMillis
                        return gc.timeInMillis
                    }
                }
            }
            return calendar.timeInMillis
        }

        @JvmStatic
        fun milli_to_iso(milli: Long): String = DateTimeFormatter.ISO_INSTANT.format(
                                                    Instant.ofEpochMilli(milli))

        @JvmStatic
        fun closest_hour(epochMilliSecond: Long): String {
            val calendar = Calendar.getInstance().apply{
                timeInMillis = epochMilliSecond
            }
            if (calendar.get(Calendar.MINUTE) >= 30) {
                calendar.roll(Calendar.HOUR, 1)
            }
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochMilli(calendar.timeInMillis))
        }

        @JvmStatic
        fun timedelta_iso(min: Int): String {
            val calendar = Calendar.getInstance()
            calendar.roll(Calendar.MINUTE, min)
            return DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochMilli(calendar.timeInMillis))
        }

        @JvmStatic
        fun timedelta_milli(min: Int): Long {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + min)
            return calendar.timeInMillis
        }

    }
}