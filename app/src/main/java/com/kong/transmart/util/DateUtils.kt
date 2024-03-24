package com.kong.transmart.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Calendar.HOUR_OF_DAY
import java.util.Date
import java.util.TimeZone

object DateUtils {
    private val TAG = "DateUtils"
    private val kstTimeZone = TimeZone.getTimeZone("Asia/Seoul")
    private val dateFormat = SimpleDateFormat("yyyyMMdd").apply {
        timeZone = kstTimeZone
    }

    private fun getCleanDateCalendar(date: Date): Calendar {
        val calendar = Calendar.getInstance(kstTimeZone)
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    fun dateToString(date: Date): String = dateFormat.format(date)

    fun stringToDate(dateString: String): Date {
        try {
            val date = dateFormat.parse(dateString)
            val calendar = getCleanDateCalendar(date)
            return calendar.time
        } catch (exception: Exception) {
            Log.e(TAG, "Failed to parse the date string($dateString): $exception")

            return getToday()
        }
    }

    fun dateToLong(date: Date): Long {
        val calendar = getCleanDateCalendar(date)
        return calendar.timeInMillis
    }

    fun longToDate(timestamp: Long): Date {
        val calendar = Calendar.getInstance(kstTimeZone)
        calendar.timeInMillis = timestamp
        return calendar.time
    }

    fun getToday(): Date {
        val calendar = getCleanDateCalendar(Date())
        return calendar.time
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        return dateToString(date1) == dateToString(date2)
    }

    fun getPreviousDate(date: Date): Date {
        val calendar = getCleanDateCalendar(date)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.time
    }

    fun getNextDate(date: Date): Date {
        val calendar = getCleanDateCalendar(date)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.time
    }

    private fun getDatesFromDayAfterStartToToday(startCalendar: Calendar): List<Date> {
        val dates = mutableListOf<Date>()
        val endCalendar = getCleanDateCalendar(Date())

        startCalendar.add(Calendar.DAY_OF_MONTH, 1)

        while (!startCalendar.after(endCalendar)) {
            dates.add(startCalendar.time)
            startCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }

    fun getLastWeekDatesToToday(): List<Date> {
        val startCalendar = getCleanDateCalendar(Date())
        startCalendar.add(Calendar.WEEK_OF_YEAR, -1)

        return getDatesFromDayAfterStartToToday(startCalendar)
    }

    fun getLastMonthDatesToToday(): List<Date> {
        val startCalendar = getCleanDateCalendar(Date())
        startCalendar.add(Calendar.MONTH, -1)

        return getDatesFromDayAfterStartToToday(startCalendar)
    }

    fun getLastYearDatesToToday(): List<Date> {
        val startCalendar = getCleanDateCalendar(Date())
        startCalendar.add(Calendar.YEAR, -1)

        return getDatesFromDayAfterStartToToday(startCalendar)
    }

    fun calculateDelayUntil(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance(kstTimeZone)
        val nextRun = getCleanDateCalendar(Date()).apply {
            this.add(HOUR_OF_DAY, hour)
            this.add(Calendar.MINUTE, minute)
            if (before(now)) {
                this.add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        return nextRun.timeInMillis - now.timeInMillis
    }
}