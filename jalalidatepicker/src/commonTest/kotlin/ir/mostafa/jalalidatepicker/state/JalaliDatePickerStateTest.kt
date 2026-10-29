package ir.mostafa.jalalidatepicker.state

import ir.mostafa.jalalidatepicker.calendar.CalendarDate
import ir.mostafa.jalalidatepicker.calendar.CalendarType
import ir.mostafa.jalalidatepicker.calendar.DayOfWeek
import ir.mostafa.jalalidatepicker.model.DatePickerEvent
import ir.mostafa.jalalidatepicker.model.SelectionMode
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class JalaliDatePickerStateTest {

    @Test
    fun singleSelection_updatesFlow() = runTest {
        val state = JalaliDatePickerState(
            calendarType = CalendarType.JALALI,
            selectionMode = SelectionMode.SINGLE,
        )
        val date = CalendarDate(1403, 1, 10)
        state.select(date)
        assertEquals(date, state.selectedDate.value)
    }

    @Test
    fun rangeSelection_buildsInclusiveRange() = runTest {
        val state = JalaliDatePickerState(selectionMode = SelectionMode.RANGE)
        state.select(CalendarDate(1403, 1, 10))
        state.select(CalendarDate(1403, 1, 5))
        val range = state.selectedRange.value
        assertNotNull(range)
        assertEquals(CalendarDate(1403, 1, 5), range.start)
        assertEquals(CalendarDate(1403, 1, 10), range.end)
        assertNull(state.rangeStartPending.value)
    }

    @Test
    fun defaultFirstDayOfWeek_followsCalendarType() {
        val jalali = JalaliDatePickerState(calendarType = CalendarType.JALALI)
        assertEquals(DayOfWeek.SATURDAY, jalali.firstDayOfWeek.value)

        val gregorian = JalaliDatePickerState(calendarType = CalendarType.GREGORIAN)
        assertEquals(DayOfWeek.SUNDAY, gregorian.firstDayOfWeek.value)
    }

    @Test
    fun confirm_emitsSharedFlowEvent() = runTest(UnconfinedTestDispatcher()) {
        val state = JalaliDatePickerState(selectionMode = SelectionMode.SINGLE)
        val date = CalendarDate(1403, 2, 1)
        state.select(date)
        var event: DatePickerEvent? = null
        val job = launch { state.selectionEvents.collect { event = it } }
        state.confirm()
        assertTrue(event is DatePickerEvent.DateConfirmed)
        assertEquals(date, (event as DatePickerEvent.DateConfirmed).date)
        job.cancel()
    }

    @Test
    fun setCalendarType_convertsSelection() {
        val state = JalaliDatePickerState(
            calendarType = CalendarType.JALALI,
            selectedDate = CalendarDate(1403, 1, 1),
        )
        state.setCalendarType(CalendarType.GREGORIAN)
        assertEquals(CalendarType.GREGORIAN, state.calendarType.value)
        assertEquals(CalendarDate(2024, 3, 20), state.selectedDate.value)
        assertEquals(DayOfWeek.SUNDAY, state.firstDayOfWeek.value)
    }
}
