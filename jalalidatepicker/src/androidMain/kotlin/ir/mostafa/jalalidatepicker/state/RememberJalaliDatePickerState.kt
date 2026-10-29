package ir.mostafa.jalalidatepicker.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import ir.mostafa.jalalidatepicker.calendar.CalendarDate
import ir.mostafa.jalalidatepicker.calendar.CalendarDateRange
import ir.mostafa.jalalidatepicker.calendar.CalendarType
import ir.mostafa.jalalidatepicker.calendar.DayOfWeek
import ir.mostafa.jalalidatepicker.calendar.YearMonth
import ir.mostafa.jalalidatepicker.model.DigitMode
import ir.mostafa.jalalidatepicker.model.SelectionMode

/**
 * Remembers a [JalaliDatePickerState] across recompositions and process death
 * (selected values + calendar type + displayed month).
 */
@Composable
fun rememberJalaliDatePickerState(
    calendarType: CalendarType = CalendarType.JALALI,
    selectionMode: SelectionMode = SelectionMode.SINGLE,
    selectedDate: CalendarDate? = null,
    selectedRange: CalendarDateRange? = null,
    displayedMonth: YearMonth? = null,
    firstDayOfWeek: DayOfWeek? = null,
    minDate: CalendarDate? = null,
    maxDate: CalendarDate? = null,
    digitMode: DigitMode = DigitMode.PERSIAN,
): JalaliDatePickerState {
    return rememberSaveable(
        calendarType,
        selectionMode,
        saver = JalaliDatePickerStateSaver,
    ) {
        JalaliDatePickerState(
            calendarType = calendarType,
            selectionMode = selectionMode,
            selectedDate = selectedDate,
            selectedRange = selectedRange,
            displayedMonth = displayedMonth,
            firstDayOfWeek = firstDayOfWeek,
            minDate = minDate,
            maxDate = maxDate,
            digitMode = digitMode,
        )
    }
}

private val JalaliDatePickerStateSaver: Saver<JalaliDatePickerState, List<Any?>> = Saver(
    save = { state ->
        listOf(
            state.calendarType.value.name,
            state.selectionMode.value.name,
            state.selectedDate.value?.year,
            state.selectedDate.value?.month,
            state.selectedDate.value?.day,
            state.selectedRange.value?.start?.year,
            state.selectedRange.value?.start?.month,
            state.selectedRange.value?.start?.day,
            state.selectedRange.value?.end?.year,
            state.selectedRange.value?.end?.month,
            state.selectedRange.value?.end?.day,
            state.displayedMonth.value.year,
            state.displayedMonth.value.month,
            state.firstDayOfWeek.value.name,
            state.digitMode.value.name,
            state.minDate.value?.year,
            state.minDate.value?.month,
            state.minDate.value?.day,
            state.maxDate.value?.year,
            state.maxDate.value?.month,
            state.maxDate.value?.day,
        )
    },
    restore = { saved ->
        fun dateAt(y: Int, m: Int, d: Int): CalendarDate? {
            val year = saved[y] as Int? ?: return null
            val month = saved[m] as Int? ?: return null
            val day = saved[d] as Int? ?: return null
            return CalendarDate(year, month, day)
        }
        JalaliDatePickerState(
            calendarType = CalendarType.valueOf(saved[0] as String),
            selectionMode = SelectionMode.valueOf(saved[1] as String),
            selectedDate = dateAt(2, 3, 4),
            selectedRange = run {
                val start = dateAt(5, 6, 7)
                val end = dateAt(8, 9, 10)
                if (start != null && end != null) CalendarDateRange(start, end) else null
            },
            displayedMonth = YearMonth(saved[11] as Int, saved[12] as Int),
            firstDayOfWeek = DayOfWeek.valueOf(saved[13] as String),
            digitMode = DigitMode.valueOf(saved[14] as String),
            minDate = dateAt(15, 16, 17),
            maxDate = dateAt(18, 19, 20),
        )
    },
)
