package ir.mostafa.jalalidatepicker.calendar

/**
 * Gregorian calendar helpers.
 * Full implementation lands in the calendar-core commit.
 */
object GregorianCalendar {
    fun daysInMonth(year: Int, month: Int): Int {
        require(month in 1..12)
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            else -> if (isLeapYear(year)) 29 else 28
        }
    }

    fun isLeapYear(year: Int): Boolean =
        (year % 4 == 0 && year % 100 != 0) || year % 400 == 0

    fun today(): CalendarDate = CalendarDate(2024, 3, 20)
}
