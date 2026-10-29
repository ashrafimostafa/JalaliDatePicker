package ir.mostafa.jalalidatepicker.ui.factory

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.LayoutDirection
import ir.mostafa.jalalidatepicker.calendar.CalendarType

@Immutable
data class JalaliDatePickerLayoutConfig(
    val layoutDirection: LayoutDirection,
    val showModeToggle: Boolean,
    val showActions: Boolean,
    val showDivider: Boolean,
    val usePersianLabels: Boolean,
)

object JalaliDatePickerLayoutFactory {
    /**
     * Jalali defaults to [LayoutDirection.Rtl]; Gregorian to [LayoutDirection.Ltr].
     * Pass [forceDirection] to override.
     */
    fun create(
        calendarType: CalendarType,
        showModeToggle: Boolean = true,
        showActions: Boolean = false,
        showDivider: Boolean = true,
        usePersianLabels: Boolean = true,
        forceDirection: LayoutDirection? = null,
    ): JalaliDatePickerLayoutConfig = JalaliDatePickerLayoutConfig(
        layoutDirection = forceDirection ?: when (calendarType) {
            CalendarType.JALALI -> LayoutDirection.Rtl
            CalendarType.GREGORIAN -> LayoutDirection.Ltr
        },
        showModeToggle = showModeToggle,
        showActions = showActions,
        showDivider = showDivider,
        usePersianLabels = usePersianLabels,
    )
}
