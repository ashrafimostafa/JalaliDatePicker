package ir.mostafa.jalalidatepicker.model

import ir.mostafa.jalalidatepicker.calendar.CalendarDate
import ir.mostafa.jalalidatepicker.calendar.CalendarDateRange

/**
 * One-shot events emitted through [ir.mostafa.jalalidatepicker.state.JalaliDatePickerState.selectionEvents].
 */
sealed interface DatePickerEvent {
    data class DateConfirmed(val date: CalendarDate) : DatePickerEvent

    data class RangeConfirmed(val range: CalendarDateRange) : DatePickerEvent

    data object Cancelled : DatePickerEvent

    /** Emitted when a range start is chosen but end is still pending. */
    data class RangeStartSelected(val start: CalendarDate) : DatePickerEvent
}
