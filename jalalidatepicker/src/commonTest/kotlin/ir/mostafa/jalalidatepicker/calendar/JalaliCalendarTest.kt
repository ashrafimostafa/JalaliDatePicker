package ir.mostafa.jalalidatepicker.calendar

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JalaliCalendarTest {

    @Test
    fun knownConversion_nowruz1403() {
        val jalali = CalendarDate(1403, 1, 1)
        val gregorian = JalaliCalendar.toGregorian(jalali)
        assertEquals(CalendarDate(2024, 3, 20), gregorian)
        assertEquals(jalali, JalaliCalendar.fromGregorian(gregorian))
    }

    @Test
    fun knownConversion_midYear() {
        val jalali = CalendarDate(1400, 6, 15)
        val gregorian = JalaliCalendar.toGregorian(jalali)
        assertEquals(jalali, JalaliCalendar.fromGregorian(gregorian))
    }

    @Test
    fun leapYear_esfandHas30Days() {
        // 1399 is a Jalali leap year
        assertTrue(JalaliCalendar.isLeapYear(1399))
        assertEquals(30, JalaliCalendar.daysInMonth(1399, 12))
        assertTrue(JalaliCalendar.isValid(CalendarDate(1399, 12, 30)))
    }

    @Test
    fun nonLeapYear_esfandHas29Days() {
        assertFalse(JalaliCalendar.isLeapYear(1400))
        assertEquals(29, JalaliCalendar.daysInMonth(1400, 12))
        assertFalse(JalaliCalendar.isValid(CalendarDate(1400, 12, 30)))
    }

    @Test
    fun roundTrip_rangeOfYears() {
        for (jy in 1390..1410) {
            for (jm in 1..12) {
                val jd = JalaliCalendar.daysInMonth(jy, jm)
                val original = CalendarDate(jy, jm, jd)
                val back = JalaliCalendar.fromGregorian(JalaliCalendar.toGregorian(original))
                assertEquals(original, back, "Failed for $original")
            }
        }
    }

    @Test
    fun plusMonths_clampsDay() {
        val date = CalendarDate(1403, 1, 31)
        val next = JalaliCalendar.plusMonths(date, 6) // Mehr has 30 days
        assertEquals(CalendarDate(1403, 7, 30), next)
    }

    @Test
    fun dayOfWeek_matchesGregorian() {
        val j = CalendarDate(1403, 1, 1)
        val g = JalaliCalendar.toGregorian(j)
        assertEquals(GregorianCalendar.dayOfWeek(g), JalaliCalendar.dayOfWeek(j))
    }
}

class GregorianCalendarTest {

    @Test
    fun leapYear_february() {
        assertTrue(GregorianCalendar.isLeapYear(2024))
        assertEquals(29, GregorianCalendar.daysInMonth(2024, 2))
        assertFalse(GregorianCalendar.isLeapYear(2023))
        assertEquals(28, GregorianCalendar.daysInMonth(2023, 2))
    }

    @Test
    fun julianDay_roundTrip() {
        val date = CalendarDate(2024, 3, 20)
        val jdn = GregorianCalendar.toJulianDayNumber(date)
        assertEquals(date, GregorianCalendar.fromJulianDayNumber(jdn))
    }

    @Test
    fun dayOfWeek_knownSaturday() {
        // 2024-03-20 was a Wednesday
        assertEquals(DayOfWeek.WEDNESDAY, GregorianCalendar.dayOfWeek(CalendarDate(2024, 3, 20)))
    }
}
