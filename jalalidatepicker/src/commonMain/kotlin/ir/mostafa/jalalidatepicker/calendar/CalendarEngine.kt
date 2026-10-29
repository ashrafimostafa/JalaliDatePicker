package ir.mostafa.jalalidatepicker.calendar

/**
 * Facade over Jalali / Gregorian engines for the active [CalendarType].
 */
object CalendarEngine {

    fun today(type: CalendarType): CalendarDate = when (type) {
        CalendarType.JALALI -> JalaliCalendar.today()
        CalendarType.GREGORIAN -> GregorianCalendar.today()
    }

    fun daysInMonth(type: CalendarType, year: Int, month: Int): Int = when (type) {
        CalendarType.JALALI -> JalaliCalendar.daysInMonth(year, month)
        CalendarType.GREGORIAN -> GregorianCalendar.daysInMonth(year, month)
    }

    fun isLeapYear(type: CalendarType, year: Int): Boolean = when (type) {
        CalendarType.JALALI -> JalaliCalendar.isLeapYear(year)
        CalendarType.GREGORIAN -> GregorianCalendar.isLeapYear(year)
    }

    fun isValid(type: CalendarType, date: CalendarDate): Boolean = when (type) {
        CalendarType.JALALI -> JalaliCalendar.isValid(date)
        CalendarType.GREGORIAN -> GregorianCalendar.isValid(date)
    }

    fun dayOfWeek(type: CalendarType, date: CalendarDate): DayOfWeek = when (type) {
        CalendarType.JALALI -> JalaliCalendar.dayOfWeek(date)
        CalendarType.GREGORIAN -> GregorianCalendar.dayOfWeek(date)
    }

    fun plusDays(type: CalendarType, date: CalendarDate, days: Int): CalendarDate = when (type) {
        CalendarType.JALALI -> JalaliCalendar.plusDays(date, days)
        CalendarType.GREGORIAN -> GregorianCalendar.plusDays(date, days)
    }

    fun plusMonths(type: CalendarType, date: CalendarDate, months: Int): CalendarDate = when (type) {
        CalendarType.JALALI -> JalaliCalendar.plusMonths(date, months)
        CalendarType.GREGORIAN -> GregorianCalendar.plusMonths(date, months)
    }

    fun convert(date: CalendarDate, from: CalendarType, to: CalendarType): CalendarDate {
        if (from == to) return date
        return when {
            from == CalendarType.JALALI && to == CalendarType.GREGORIAN ->
                JalaliCalendar.toGregorian(date)
            from == CalendarType.GREGORIAN && to == CalendarType.JALALI ->
                JalaliCalendar.fromGregorian(date)
            else -> date
        }
    }

    fun dayOffset(day: DayOfWeek, firstDayOfWeek: DayOfWeek): Int =
        JalaliCalendar.dayOffset(day, firstDayOfWeek)
}
