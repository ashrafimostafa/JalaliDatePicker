package ir.mostafa.jalalidatepicker.calendar

/**
 * Jalali (Persian / Solar Hijri) calendar arithmetic and conversion.
 *
 * Conversion follows the Jalaali algorithm (jalaali-js compatible).
 */
object JalaliCalendar {

    private val breaks = intArrayOf(
        -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181,
        1210, 1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178,
    )

    fun daysInMonth(year: Int, month: Int): Int {
        require(month in 1..12) { "Month must be in 1..12, was $month" }
        return when {
            month <= 6 -> 31
            month <= 11 -> 30
            else -> if (isLeapYear(year)) 30 else 29
        }
    }

    fun isLeapYear(year: Int): Boolean = jalCal(year).leap == 0

    fun isValid(date: CalendarDate): Boolean {
        if (date.month !in 1..12) return false
        return date.day in 1..daysInMonth(date.year, date.month)
    }

    fun requireValid(date: CalendarDate): CalendarDate {
        require(isValid(date)) { "Invalid Jalali date: $date" }
        return date
    }

    fun today(): CalendarDate = fromGregorian(GregorianCalendar.today())

    fun dayOfWeek(date: CalendarDate): DayOfWeek =
        GregorianCalendar.dayOfWeek(toGregorian(date))

    fun toGregorian(jalali: CalendarDate): CalendarDate {
        requireValid(jalali)
        val parts = jalaliToGregorian(jalali.year, jalali.month, jalali.day)
        return CalendarDate(parts[0], parts[1], parts[2])
    }

    fun fromGregorian(gregorian: CalendarDate): CalendarDate {
        GregorianCalendar.requireValid(gregorian)
        val parts = gregorianToJalali(gregorian.year, gregorian.month, gregorian.day)
        return CalendarDate(parts[0], parts[1], parts[2])
    }

    fun plusDays(date: CalendarDate, days: Int): CalendarDate =
        fromGregorian(GregorianCalendar.plusDays(toGregorian(date), days))

    fun plusMonths(date: CalendarDate, months: Int): CalendarDate {
        val ym = YearMonth.from(date).plusMonths(months)
        val day = minOf(date.day, daysInMonth(ym.year, ym.month))
        return CalendarDate(ym.year, ym.month, day)
    }

    fun daysBetween(start: CalendarDate, end: CalendarDate): Int =
        GregorianCalendar.daysBetween(toGregorian(start), toGregorian(end))

    fun dayOffset(day: DayOfWeek, firstDayOfWeek: DayOfWeek): Int {
        val diff = day.isoValue - firstDayOfWeek.isoValue
        return ((diff % 7) + 7) % 7
    }

    // region Jalaali algorithm (jalaali-js)

    private data class JalCalResult(
        val leap: Int,
        val gy: Int,
        val march: Int,
    )

    private fun div(a: Int, b: Int): Int = a / b

    private fun mod(a: Int, b: Int): Int = a % b

    private fun jalCal(jy: Int): JalCalResult {
        val bl = breaks.size
        val gy = jy + 621
        var leapJ = -14
        var jp = breaks[0]
        var jump = 0
        var i = 1
        while (i < bl) {
            val jm = breaks[i]
            jump = jm - jp
            if (jy < jm) break
            leapJ += div(jump, 33) * 8 + div(mod(jump, 33), 4)
            jp = jm
            i++
        }
        var n = jy - jp
        leapJ += div(n, 33) * 8 + div(mod(n, 33) + 3, 4)
        if (mod(jump, 33) == 4 && jump - n == 4) leapJ += 1

        val leapG = div(gy, 4) - div((div(gy, 100) + 1) * 3, 4) - 150
        val march = 20 + leapJ - leapG

        if (jump - n < 6) {
            n = n - jump + div(jump + 4, 33) * 33
        }
        var leap = mod(mod(n + 1, 33) - 1, 4)
        if (leap < 0) leap += 4

        return JalCalResult(leap = leap, gy = gy, march = march)
    }

    private fun g2d(gy: Int, gm: Int, gd: Int): Int {
        var d = div((gy + div(gm - 8, 6) + 100100) * 1461, 4) +
            div(153 * mod(gm + 9, 12) + 2, 5) +
            gd - 34840408
        d = d - div(div(gy + 100100 + div(gm - 8, 6), 100) * 3, 4) + 752
        return d
    }

    private fun d2g(jdn: Int): IntArray {
        var j = 4 * jdn + 139361631
        j += div(div(4 * jdn + 183187720, 146097) * 3, 4) * 4 - 3908
        val i = div(mod(j, 1461), 4) * 5 + 308
        val gd = div(mod(i, 153), 5) + 1
        val gm = mod(div(i, 153), 12) + 1
        val gy = div(j, 1461) - 100100 + div(8 - gm, 6)
        return intArrayOf(gy, gm, gd)
    }

    private fun jalaliToGregorian(jy: Int, jm: Int, jd: Int): IntArray {
        val r = jalCal(jy)
        val jdn = g2d(r.gy, 3, r.march) + (jm - 1) * 31 - div(jm, 7) * (jm - 7) + jd - 1
        return d2g(jdn)
    }

    private fun gregorianToJalali(gy: Int, gm: Int, gd: Int): IntArray {
        var jy = gy - 621
        val r = jalCal(jy)
        val jdn1f = g2d(gy, 3, r.march)
        val jdn = g2d(gy, gm, gd)
        var k = jdn - jdn1f
        if (k >= 0) {
            if (k <= 185) {
                val jm = 1 + div(k, 31)
                val jd = mod(k, 31) + 1
                return intArrayOf(jy, jm, jd)
            } else {
                k -= 186
            }
        } else {
            jy -= 1
            k += 179
            if (r.leap == 1) k += 1
        }
        val jm = 7 + div(k, 30)
        val jd = mod(k, 30) + 1
        return intArrayOf(jy, jm, jd)
    }

    // endregion
}
