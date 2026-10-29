package ir.mostafa.jalalidatepicker.calendar

/**
 * Supported calendar systems.
 *
 * Default first day of week:
 * - [JALALI] → Saturday
 * - [GREGORIAN] → Sunday
 */
enum class CalendarType {
    JALALI,
    GREGORIAN,
    ;

    val defaultFirstDayOfWeek: DayOfWeek
        get() = when (this) {
            JALALI -> DayOfWeek.SATURDAY
            GREGORIAN -> DayOfWeek.SUNDAY
        }
}
