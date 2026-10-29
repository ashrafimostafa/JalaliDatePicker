package ir.mostafa.jalalidatepicker.calendar

/**
 * Year + month pair used for displayed-month navigation.
 */
data class YearMonth(
    val year: Int,
    val month: Int,
) {
    init {
        require(month in 1..12) { "Month must be in 1..12, was $month" }
    }

    fun plusMonths(months: Int): YearMonth {
        val total = year * 12L + (month - 1) + months
        val newYear = Math.floorDiv(total, 12).toInt()
        val newMonth = Math.floorMod(total, 12).toInt() + 1
        return YearMonth(newYear, newMonth)
    }

    companion object {
        fun from(date: CalendarDate): YearMonth = YearMonth(date.year, date.month)
    }
}

// Multiplatform-friendly floor helpers without java.lang.Math on non-JVM
private object Math {
    fun floorDiv(a: Long, b: Long): Long {
        var q = a / b
        if ((a xor b) < 0 && q * b != a) q--
        return q
    }

    fun floorMod(a: Long, b: Long): Long = a - floorDiv(a, b) * b
}
