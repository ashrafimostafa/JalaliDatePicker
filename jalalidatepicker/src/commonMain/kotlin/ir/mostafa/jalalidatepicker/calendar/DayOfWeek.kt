package ir.mostafa.jalalidatepicker.calendar

/**
 * ISO-like day-of-week where Monday = 1 … Sunday = 7.
 * Matches [java.time.DayOfWeek] numbering for interoperability.
 */
enum class DayOfWeek(val isoValue: Int) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7),
    ;

    companion object {
        fun fromIso(value: Int): DayOfWeek {
            require(value in 1..7) { "Day of week must be in 1..7, was $value" }
            return entries.first { it.isoValue == value }
        }
    }
}
