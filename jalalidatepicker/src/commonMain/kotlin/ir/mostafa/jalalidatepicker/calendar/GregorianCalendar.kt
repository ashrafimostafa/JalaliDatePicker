package ir.mostafa.jalalidatepicker.calendar

/**
 * Gregorian civil calendar arithmetic.
 */
object GregorianCalendar {

    private val monthDaysCommon = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val monthDaysLeap = intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    fun isLeapYear(year: Int): Boolean =
        (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

    fun daysInMonth(year: Int, month: Int): Int {
        require(month in 1..12) { "Month must be in 1..12, was $month" }
        return if (isLeapYear(year)) monthDaysLeap[month - 1] else monthDaysCommon[month - 1]
    }

    fun isValid(date: CalendarDate): Boolean {
        if (date.month !in 1..12) return false
        return date.day in 1..daysInMonth(date.year, date.month)
    }

    fun requireValid(date: CalendarDate): CalendarDate {
        require(isValid(date)) { "Invalid Gregorian date: $date" }
        return date
    }

    fun today(): CalendarDate = currentGregorianDate()

    /**
     * ISO day of week for a Gregorian date (Monday = 1 … Sunday = 7).
     * Uses Sakamoto's methods.
     */
    fun dayOfWeek(date: CalendarDate): DayOfWeek {
        requireValid(date)
        var y = date.year
        val m = date.month
        val d = date.day
        val t = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
        if (m < 3) y -= 1
        val sakamoto = (y + y / 4 - y / 100 + y / 400 + t[m - 1] + d) % 7
        // Sakamoto: 0 = Sunday … 6 = Saturday → convert to ISO
        val iso = if (sakamoto == 0) 7 else sakamoto
        return DayOfWeek.fromIso(iso)
    }

    fun toJulianDayNumber(date: CalendarDate): Int {
        requireValid(date)
        val y = date.year
        val m = date.month
        val d = date.day
        val a = (14 - m) / 12
        val yy = y + 4800 - a
        val mm = m + 12 * a - 3
        return d + (153 * mm + 2) / 5 + 365 * yy + yy / 4 - yy / 100 + yy / 400 - 32045
    }

    fun fromJulianDayNumber(jdn: Int): CalendarDate {
        val a = jdn + 32044
        val b = (4 * a + 3) / 146097
        val c = a - (146097 * b) / 4
        val d = (4 * c + 3) / 1461
        val e = c - (1461 * d) / 4
        val m = (5 * e + 2) / 153
        val day = e - (153 * m + 2) / 5 + 1
        val month = m + 3 - 12 * (m / 10)
        val year = 100 * b + d - 4800 + m / 10
        return CalendarDate(year, month, day)
    }

    fun plusDays(date: CalendarDate, days: Int): CalendarDate =
        fromJulianDayNumber(toJulianDayNumber(date) + days)

    fun plusMonths(date: CalendarDate, months: Int): CalendarDate {
        val ym = YearMonth.from(date).plusMonths(months)
        val day = minOf(date.day, daysInMonth(ym.year, ym.month))
        return CalendarDate(ym.year, ym.month, day)
    }

    fun daysBetween(start: CalendarDate, end: CalendarDate): Int =
        toJulianDayNumber(end) - toJulianDayNumber(start)
}
