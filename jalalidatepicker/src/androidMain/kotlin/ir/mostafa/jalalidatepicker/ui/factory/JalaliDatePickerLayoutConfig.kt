package ir.mostafa.jalalidatepicker.ui.factory

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.LayoutDirection
import ir.mostafa.jalalidatepicker.calendar.CalendarType
import ir.mostafa.jalalidatepicker.model.MonthCountMode

@Immutable
data class JalaliDatePickerLayoutConfig(
    val layoutDirection: LayoutDirection,
    val showActions: Boolean,
    val showDivider: Boolean,
    val usePersianLabels: Boolean,
    val showTodayButton: Boolean,
    val enableMonthSwipe: Boolean,
    val monthCountMode: MonthCountMode,
)

object JalaliDatePickerLayoutFactory {
    /**
     * Jalali defaults to [LayoutDirection.Rtl]; Gregorian to [LayoutDirection.Ltr].
     * Calendar type is set in code via [ir.mostafa.jalalidatepicker.state.JalaliDatePickerState] —
     * there is no in-UI Jalali/Miladi toggle.
     */
    fun create(
        calendarType: CalendarType,
        showActions: Boolean = false,
        showDivider: Boolean = true,
        usePersianLabels: Boolean = true,
        showTodayButton: Boolean = true,
        enableMonthSwipe: Boolean = true,
        monthCountMode: MonthCountMode = MonthCountMode.Auto,
        forceDirection: LayoutDirection? = null,
    ): JalaliDatePickerLayoutConfig = JalaliDatePickerLayoutConfig(
        layoutDirection = forceDirection ?: when (calendarType) {
            CalendarType.JALALI -> LayoutDirection.Rtl
            CalendarType.GREGORIAN -> LayoutDirection.Ltr
        },
        showActions = showActions,
        showDivider = showDivider,
        usePersianLabels = usePersianLabels,
        showTodayButton = showTodayButton,
        enableMonthSwipe = enableMonthSwipe,
        monthCountMode = monthCountMode,
    )
}
