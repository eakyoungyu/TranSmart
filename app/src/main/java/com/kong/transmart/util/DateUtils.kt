package com.kong.transmart.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

object DateUtils {
    private val dateFormat = SimpleDateFormat("yyyyMMdd").apply {
        timeZone = TimeZone.getTimeZone("Asia/Seoul")
    }

    fun dateToString(date: Date): String = dateFormat.format(date)

    fun stringToDate(dateString: String): Date = dateFormat.parse(dateString)!!

    fun getTodayInKST(): String = dateToString(Date())

    fun getDateInKST(days: Int): Date {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        calendar.add(Calendar.DAY_OF_MONTH, days)
        return calendar.time
    }

    private fun getDatesFromDayAfterStartToToday(startCalendar: Calendar): List<Date> {
        val dates = mutableListOf<Date>()
        val endCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))

        startCalendar.add(Calendar.DAY_OF_MONTH, 1)

        while (!startCalendar.after(endCalendar)) {
            dates.add(startCalendar.time)
            startCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }

    fun getLastWeekDatesToToday(): List<Date> {
        val startCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        startCalendar.add(Calendar.WEEK_OF_YEAR, -1)

        return getDatesFromDayAfterStartToToday(startCalendar)
    }

    fun getLastMonthDatesToToday(): List<Date> {
        val startCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        startCalendar.add(Calendar.MONTH, -1)

        return getDatesFromDayAfterStartToToday(startCalendar)
    }

    fun getLastYearDatesToToday(): List<Date> {
        val startCalendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        startCalendar.add(Calendar.YEAR, -1)

        return getDatesFromDayAfterStartToToday(startCalendar)
    }

}