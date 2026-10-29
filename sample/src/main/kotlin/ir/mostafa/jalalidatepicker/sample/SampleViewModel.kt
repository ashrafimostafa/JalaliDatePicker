package ir.mostafa.jalalidatepicker.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.mostafa.jalalidatepicker.calendar.CalendarDate
import ir.mostafa.jalalidatepicker.calendar.CalendarDateRange
import ir.mostafa.jalalidatepicker.model.DatePickerEvent
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.model.SelectionMode
import ir.mostafa.jalalidatepicker.state.JalaliDatePickerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SampleViewModel : ViewModel() {

    val pickerState = JalaliDatePickerState(
        selectionMode = SelectionMode.SINGLE,
    )

    val selectedDate: StateFlow<CalendarDate?> = pickerState.selectedDate
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val selectedRange: StateFlow<CalendarDateRange?> = pickerState.selectedRange
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _lastEvent = MutableStateFlow<String>("—")
    val lastEvent: StateFlow<String> = _lastEvent.asStateFlow()

    private val _darkTheme = MutableStateFlow(false)
    val darkTheme: StateFlow<Boolean> = _darkTheme.asStateFlow()

    private val _pickerSize = MutableStateFlow(DatePickerSize.Medium)
    val pickerSize: StateFlow<DatePickerSize> = _pickerSize.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _showSheet = MutableStateFlow(false)
    val showSheet: StateFlow<Boolean> = _showSheet.asStateFlow()

    init {
        viewModelScope.launch {
            pickerState.selectionEvents.collect { event ->
                _lastEvent.value = when (event) {
                    is DatePickerEvent.DateConfirmed ->
                        "Confirmed: ${event.date.year}/${event.date.month}/${event.date.day}"
                    is DatePickerEvent.RangeConfirmed ->
                        "Range: ${event.range.start} → ${event.range.end}"
                    is DatePickerEvent.RangeStartSelected ->
                        "Range start: ${event.start}"
                    DatePickerEvent.Cancelled -> "Cancelled"
                }
            }
        }
    }

    fun toggleTheme() {
        _darkTheme.value = !_darkTheme.value
    }

    fun cycleSize() {
        _pickerSize.value = when (_pickerSize.value) {
            DatePickerSize.Compact -> DatePickerSize.Medium
            DatePickerSize.Medium -> DatePickerSize.Large
            DatePickerSize.Large -> DatePickerSize.Compact
        }
    }

    fun setSelectionMode(mode: SelectionMode) {
        pickerState.setSelectionMode(mode)
    }

    fun openDialog() {
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
    }

    fun openSheet() {
        _showSheet.value = true
    }

    fun closeSheet() {
        _showSheet.value = false
    }
}
