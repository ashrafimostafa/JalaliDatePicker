package ir.mostafa.jalalidatepicker.calendar

import java.util.Calendar
import java.util.TimeZone

internal actual fun currentGregorianDate(): CalendarDate {
    val cal = Calendar.getInstance(TimeZone.getDefault())
    return CalendarDate(
        year = cal.get(Calendar.YEAR),
        month = cal.get(Calendar.MONTH) + 1,
        day = cal.get(Calendar.DAY_OF_MONTH),
    )
}
