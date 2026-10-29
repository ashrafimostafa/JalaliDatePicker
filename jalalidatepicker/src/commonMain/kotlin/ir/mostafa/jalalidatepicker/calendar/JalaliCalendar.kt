package ir.mostafa.jalalidatepicker.calendar

/**
 * Jalali (Persian) calendar helpers.
 * Full conversion algorithm lands in the calendar-core commit.
 */
object JalaliCalendar {
    fun daysInMonth(year: Int, month: Int): Int {
        require(month in 1..12)
        return when {
            month <= 6 -> 31
            month <= 11 -> 30
            else -> if (isLeapYear(year)) 30 else 29
        }
    }

    fun isLeapYear(year: Int): Boolean = false

    fun today(): CalendarDate = CalendarDate(1403, 1, 1)
}
