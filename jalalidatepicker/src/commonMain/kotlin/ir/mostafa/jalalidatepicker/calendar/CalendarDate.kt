package ir.mostafa.jalalidatepicker.calendar

/**
 * A calendar-agnostic civil date (year / month / day).
 *
 * Interpretation depends on the active [CalendarType]:
 * - Jalali months are 1..12 (Farvardin … Esfand)
 * - Gregorian months are 1..12 (January … December)
 */
data class CalendarDate(
    val year: Int,
    val month: Int,
    val day: Int,
) : Comparable<CalendarDate> {

    init {
        require(month in 1..12) { "Month must be in 1..12, was $month" }
        require(day in 1..31) { "Day must be in 1..31, was $day" }
    }

    override fun compareTo(other: CalendarDate): Int =
        compareValuesBy(this, other, CalendarDate::year, CalendarDate::month, CalendarDate::day)

    fun isBefore(other: CalendarDate): Boolean = this < other

    fun isAfter(other: CalendarDate): Boolean = this > other

    companion object {
        /** Farvardin 1, 1400 / a sensible Jalali epoch for defaults. */
        val JALALI_EPOCH: CalendarDate = CalendarDate(1400, 1, 1)

        /** January 1, 2021 — Gregorian counterpart of [JALALI_EPOCH]. */
        val GREGORIAN_EPOCH: CalendarDate = CalendarDate(2021, 3, 21)
    }
}

/**
 * Inclusive date range used by range-selection mode.
 */
data class CalendarDateRange(
    val start: CalendarDate,
    val end: CalendarDate,
) {
    init {
        require(start <= end) {
            "Range start ($start) must be on or before end ($end)"
        }
    }

    operator fun contains(date: CalendarDate): Boolean = date in start..end
}
