package ir.mostafa.jalalidatepicker.locale

import ir.mostafa.jalalidatepicker.calendar.CalendarType
import ir.mostafa.jalalidatepicker.calendar.DayOfWeek
import ir.mostafa.jalalidatepicker.model.DigitMode

object DateLabels {

    private val jalaliMonthsFa = listOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند",
    )

    private val jalaliMonthsEn = listOf(
        "Farvardin", "Ordibehesht", "Khordad", "Tir", "Mordad", "Shahrivar",
        "Mehr", "Aban", "Azar", "Dey", "Bahman", "Esfand",
    )

    private val gregorianMonthsEn = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December",
    )

    private val gregorianMonthsFa = listOf(
        "ژانویه", "فوریه", "مارس", "آوریل", "مه", "ژوئن",
        "ژوئیه", "اوت", "سپتامبر", "اکتبر", "نوامبر", "دسامبر",
    )

    private val weekdaysFaShort = mapOf(
        DayOfWeek.SATURDAY to "ش",
        DayOfWeek.SUNDAY to "ی",
        DayOfWeek.MONDAY to "د",
        DayOfWeek.TUESDAY to "س",
        DayOfWeek.WEDNESDAY to "چ",
        DayOfWeek.THURSDAY to "پ",
        DayOfWeek.FRIDAY to "ج",
    )

    private val weekdaysEnShort = mapOf(
        DayOfWeek.SATURDAY to "Sa",
        DayOfWeek.SUNDAY to "Su",
        DayOfWeek.MONDAY to "Mo",
        DayOfWeek.TUESDAY to "Tu",
        DayOfWeek.WEDNESDAY to "We",
        DayOfWeek.THURSDAY to "Th",
        DayOfWeek.FRIDAY to "Fr",
    )

    fun monthName(
        type: CalendarType,
        month: Int,
        persian: Boolean = true,
    ): String {
        require(month in 1..12)
        return when (type) {
            CalendarType.JALALI -> if (persian) jalaliMonthsFa[month - 1] else jalaliMonthsEn[month - 1]
            CalendarType.GREGORIAN -> if (persian) gregorianMonthsFa[month - 1] else gregorianMonthsEn[month - 1]
        }
    }

    fun weekdayShort(day: DayOfWeek, persian: Boolean = true): String =
        if (persian) weekdaysFaShort.getValue(day) else weekdaysEnShort.getValue(day)

    fun orderedWeekdays(firstDayOfWeek: DayOfWeek): List<DayOfWeek> {
        val all = DayOfWeek.entries
        val startIndex = all.indexOf(firstDayOfWeek)
        return all.drop(startIndex) + all.take(startIndex)
    }

    fun formatNumber(value: Int, mode: DigitMode): String {
        val raw = value.toString()
        return when (mode) {
            DigitMode.LATIN -> raw
            DigitMode.PERSIAN -> raw.map { ch ->
                if (ch in '0'..'9') ('۰' + (ch - '0')) else ch
            }.joinToString("")
        }
    }
}
