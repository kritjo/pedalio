package in2000.pedalio.utils

import android.icu.util.Calendar
import android.icu.util.TimeZone
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Utility Functions for date and time
 * ISO is ISO 8609, milli is ms since epoch
 */
class DateTime {
    companion object {

        /**
         * @param iso ISO 8609 date string
         * @return ms since epoch
         */
        @JvmStatic
        fun isoToMilli(iso: String): Long {
            val calendar = Calendar.getInstance(TimeZone.GMT_ZONE).apply {
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
                        set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY) - split[7])
                    } else if (iso.contains("-")) {
                        set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY) + split[7])
                    }
                } else {
                    set(Calendar.MILLISECOND, 0)
                    if (iso.contains("+")) {
                        set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY) - split[6])
                    } else if (iso.contains("-")) {
                        set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY) + split[6])
                    }
                }
            }
            return calendar.timeInMillis
        }

        /**
         * @param milli ms since epoch
         * @return ISO 8609 date string
         */
        @JvmStatic
        fun milliToIso(milli: Long): String = DateTimeFormatter.ISO_INSTANT.format(
                                                    Instant.ofEpochMilli(milli))

        /**
         * @param epochMilliSecond ms since epoch
         * @return ISO 8609 date string of closest hour
         */
        @JvmStatic
        fun closestHour(epochMilliSecond: Long): String {
            val calendar = Calendar.getInstance().apply{
                timeInMillis = epochMilliSecond
            }
            if (calendar.get(Calendar.MINUTE) >= 30) {
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1)
            }
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochMilli(calendar.timeInMillis))
        }

        /**
         * @param min how many minutes from now.
         * @return ISO 8609 date string of the calculated delta.
         */
        @JvmStatic
        fun timeDeltaMilli(min: Int): Long {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + min)
            return calendar.timeInMillis
        }

    }
}