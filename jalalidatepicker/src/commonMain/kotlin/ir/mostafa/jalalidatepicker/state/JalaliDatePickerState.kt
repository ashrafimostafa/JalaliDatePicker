package ir.mostafa.jalalidatepicker.state

import ir.mostafa.jalalidatepicker.calendar.CalendarDate
import ir.mostafa.jalalidatepicker.calendar.CalendarDateRange
import ir.mostafa.jalalidatepicker.calendar.CalendarEngine
import ir.mostafa.jalalidatepicker.calendar.CalendarType
import ir.mostafa.jalalidatepicker.calendar.DayOfWeek
import ir.mostafa.jalalidatepicker.calendar.YearMonth
import ir.mostafa.jalalidatepicker.model.DatePickerEvent
import ir.mostafa.jalalidatepicker.model.DigitMode
import ir.mostafa.jalalidatepicker.model.SelectionMode
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Coroutine-friendly picker state.
 *
 * Observe [selectedDate], [selectedRange], [displayedMonth], and [calendarType] via
 * [StateFlow], and listen for confirm/cancel through [selectionEvents].
 * Construction does **not** require a [kotlinx.coroutines.CoroutineScope].
 */
class JalaliDatePickerState private constructor(
    initialCalendarType: CalendarType,
    initialSelectionMode: SelectionMode,
    initialSelectedDate: CalendarDate?,
    initialSelectedRange: CalendarDateRange?,
    initialDisplayedMonth: YearMonth?,
    initialFirstDayOfWeek: DayOfWeek?,
    initialMinDate: CalendarDate?,
    initialMaxDate: CalendarDate?,
    initialDigitMode: DigitMode,
) {

    companion object {
        /**
         * Creates a [JalaliDatePickerState] for non-Compose callers (e.g. ViewModels).
         */
        operator fun invoke(
            calendarType: CalendarType = CalendarType.JALALI,
            selectionMode: SelectionMode = SelectionMode.SINGLE,
            selectedDate: CalendarDate? = null,
            selectedRange: CalendarDateRange? = null,
            displayedMonth: YearMonth? = null,
            firstDayOfWeek: DayOfWeek? = null,
            minDate: CalendarDate? = null,
            maxDate: CalendarDate? = null,
            digitMode: DigitMode = DigitMode.PERSIAN,
        ): JalaliDatePickerState = JalaliDatePickerState(
            initialCalendarType = calendarType,
            initialSelectionMode = selectionMode,
            initialSelectedDate = selectedDate,
            initialSelectedRange = selectedRange,
            initialDisplayedMonth = displayedMonth,
            initialFirstDayOfWeek = firstDayOfWeek,
            initialMinDate = minDate,
            initialMaxDate = maxDate,
            initialDigitMode = digitMode,
        )
    }
    private val _calendarType = MutableStateFlow(initialCalendarType)
    val calendarType: StateFlow<CalendarType> = _calendarType.asStateFlow()

    private val _selectionMode = MutableStateFlow(initialSelectionMode)
    val selectionMode: StateFlow<SelectionMode> = _selectionMode.asStateFlow()

    private val _selectedDate = MutableStateFlow(initialSelectedDate)
    val selectedDate: StateFlow<CalendarDate?> = _selectedDate.asStateFlow()

    private val _selectedRange = MutableStateFlow(initialSelectedRange)
    val selectedRange: StateFlow<CalendarDateRange?> = _selectedRange.asStateFlow()

    private val _rangeStartPending = MutableStateFlow<CalendarDate?>(null)
    val rangeStartPending: StateFlow<CalendarDate?> = _rangeStartPending.asStateFlow()

    private val _displayedMonth = MutableStateFlow(
        initialDisplayedMonth
            ?: YearMonth.from(
                initialSelectedDate
                    ?: initialSelectedRange?.start
                    ?: CalendarEngine.today(initialCalendarType),
            ),
    )
    val displayedMonth: StateFlow<YearMonth> = _displayedMonth.asStateFlow()

    private val _firstDayOfWeek = MutableStateFlow(
        initialFirstDayOfWeek ?: initialCalendarType.defaultFirstDayOfWeek,
    )
    val firstDayOfWeek: StateFlow<DayOfWeek> = _firstDayOfWeek.asStateFlow()

    private val _minDate = MutableStateFlow(initialMinDate)
    val minDate: StateFlow<CalendarDate?> = _minDate.asStateFlow()

    private val _maxDate = MutableStateFlow(initialMaxDate)
    val maxDate: StateFlow<CalendarDate?> = _maxDate.asStateFlow()

    private val _digitMode = MutableStateFlow(initialDigitMode)
    val digitMode: StateFlow<DigitMode> = _digitMode.asStateFlow()

    private val _selectionEvents = MutableSharedFlow<DatePickerEvent>(
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val selectionEvents: SharedFlow<DatePickerEvent> = _selectionEvents.asSharedFlow()

    fun setCalendarType(type: CalendarType, convertSelection: Boolean = true) {
        val previous = _calendarType.value
        if (previous == type) return

        if (convertSelection) {
            _selectedDate.update { date ->
                date?.let { CalendarEngine.convert(it, previous, type) }
            }
            _selectedRange.update { range ->
                range?.let {
                    CalendarDateRange(
                        start = CalendarEngine.convert(it.start, previous, type),
                        end = CalendarEngine.convert(it.end, previous, type),
                    )
                }
            }
            _rangeStartPending.update { pending ->
                pending?.let { CalendarEngine.convert(it, previous, type) }
            }
            _minDate.update { min -> min?.let { CalendarEngine.convert(it, previous, type) } }
            _maxDate.update { max -> max?.let { CalendarEngine.convert(it, previous, type) } }
            _displayedMonth.update { ym ->
                val converted = CalendarEngine.convert(
                    CalendarDate(ym.year, ym.month, 1),
                    previous,
                    type,
                )
                YearMonth(converted.year, converted.month)
            }
        }

        _calendarType.value = type
        if (_firstDayOfWeek.value == previous.defaultFirstDayOfWeek) {
            _firstDayOfWeek.value = type.defaultFirstDayOfWeek
        }
    }

    fun setSelectionMode(mode: SelectionMode) {
        if (_selectionMode.value == mode) return
        _selectionMode.value = mode
        _rangeStartPending.value = null
        when (mode) {
            SelectionMode.SINGLE -> _selectedRange.value = null
            SelectionMode.RANGE -> _selectedDate.value = null
        }
    }

    fun setFirstDayOfWeek(day: DayOfWeek) {
        _firstDayOfWeek.value = day
    }

    fun setDigitMode(mode: DigitMode) {
        _digitMode.value = mode
    }

    fun setMinDate(date: CalendarDate?) {
        _minDate.value = date
    }

    fun setMaxDate(date: CalendarDate?) {
        _maxDate.value = date
    }

    fun setDisplayedMonth(yearMonth: YearMonth) {
        _displayedMonth.value = yearMonth
    }

    fun navigateMonth(delta: Int) {
        _displayedMonth.update { it.plusMonths(delta) }
    }

    fun isSelectable(date: CalendarDate): Boolean {
        val min = _minDate.value
        val max = _maxDate.value
        if (min != null && date < min) return false
        if (max != null && date > max) return false
        return CalendarEngine.isValid(_calendarType.value, date)
    }

    /**
     * Handles a day tap according to [selectionMode].
     */
    fun select(date: CalendarDate) {
        if (!isSelectable(date)) return
        when (_selectionMode.value) {
            SelectionMode.SINGLE -> {
                _selectedDate.value = date
                _displayedMonth.value = YearMonth.from(date)
            }
            SelectionMode.RANGE -> selectRangeDay(date)
        }
    }

    private fun selectRangeDay(date: CalendarDate) {
        val pending = _rangeStartPending.value
        val current = _selectedRange.value
        when {
            pending == null && current == null -> {
                _rangeStartPending.value = date
                _selectionEvents.tryEmit(DatePickerEvent.RangeStartSelected(date))
            }
            pending == null && current != null -> {
                // Start a new range
                _selectedRange.value = null
                _rangeStartPending.value = date
                _selectionEvents.tryEmit(DatePickerEvent.RangeStartSelected(date))
            }
            pending != null -> {
                val start = minOf(pending, date)
                val end = maxOf(pending, date)
                val range = CalendarDateRange(start, end)
                _selectedRange.value = range
                _rangeStartPending.value = null
            }
        }
        _displayedMonth.value = YearMonth.from(date)
    }

    fun clearSelection() {
        _selectedDate.value = null
        _selectedRange.value = null
        _rangeStartPending.value = null
    }

    fun confirm() {
        when (_selectionMode.value) {
            SelectionMode.SINGLE -> {
                val date = _selectedDate.value ?: return
                _selectionEvents.tryEmit(DatePickerEvent.DateConfirmed(date))
            }
            SelectionMode.RANGE -> {
                val range = _selectedRange.value ?: return
                _selectionEvents.tryEmit(DatePickerEvent.RangeConfirmed(range))
            }
        }
    }

    fun cancel() {
        _selectionEvents.tryEmit(DatePickerEvent.Cancelled)
    }

    /**
     * Days to render for [yearMonth] (includes leading/trailing blanks as null).
     * Defaults to the currently displayed month.
     */
    fun monthGrid(yearMonth: YearMonth = _displayedMonth.value): List<CalendarDate?> {
        val type = _calendarType.value
        val first = CalendarDate(yearMonth.year, yearMonth.month, 1)
        val offset = CalendarEngine.dayOffset(
            CalendarEngine.dayOfWeek(type, first),
            _firstDayOfWeek.value,
        )
        val daysInMonth = CalendarEngine.daysInMonth(type, yearMonth.year, yearMonth.month)
        val cells = ArrayList<CalendarDate?>(42)
        repeat(offset) { cells.add(null) }
        for (day in 1..daysInMonth) {
            cells.add(CalendarDate(yearMonth.year, yearMonth.month, day))
        }
        while (cells.size % 7 != 0) cells.add(null)
        while (cells.size < 42) cells.add(null)
        return cells
    }
}

