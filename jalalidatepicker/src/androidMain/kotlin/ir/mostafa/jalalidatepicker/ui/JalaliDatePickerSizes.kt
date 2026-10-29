package ir.mostafa.jalalidatepicker.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.mostafa.jalalidatepicker.model.DatePickerSize

@Immutable
data class JalaliDatePickerSizes(
    val containerPadding: Dp,
    val dayCellSize: Dp,
    /** Minimum touch target for day taps (Material guidance ~48.dp). */
    val dayTouchTarget: Dp,
    val dayTextSize: TextUnit,
    val weekdayTextSize: TextUnit,
    val headlineTextSize: TextUnit,
    val titleTextSize: TextUnit,
    val navigationIconSize: Dp,
    val rowSpacing: Dp,
    /** Width at/above which [MonthCountMode.Auto] shows two months. */
    val dualMonthBreakpoint: Dp = 600.dp,
)

fun JalaliDatePickerDefaults.sizes(size: DatePickerSize = DatePickerSize.Medium): JalaliDatePickerSizes =
    when (size) {
        DatePickerSize.Small -> JalaliDatePickerSizes(
            containerPadding = 8.dp,
            dayCellSize = 32.dp,
            dayTouchTarget = 40.dp,
            dayTextSize = 12.sp,
            weekdayTextSize = 10.sp,
            headlineTextSize = 18.sp,
            titleTextSize = 11.sp,
            navigationIconSize = 18.dp,
            rowSpacing = 2.dp,
            dualMonthBreakpoint = 560.dp,
        )
        DatePickerSize.Compact -> JalaliDatePickerSizes(
            containerPadding = 12.dp,
            dayCellSize = 36.dp,
            dayTouchTarget = 44.dp,
            dayTextSize = 13.sp,
            weekdayTextSize = 11.sp,
            headlineTextSize = 20.sp,
            titleTextSize = 12.sp,
            navigationIconSize = 20.dp,
            rowSpacing = 2.dp,
        )
        DatePickerSize.Medium -> JalaliDatePickerSizes(
            containerPadding = 16.dp,
            dayCellSize = 44.dp,
            dayTouchTarget = 48.dp,
            dayTextSize = 14.sp,
            weekdayTextSize = 12.sp,
            headlineTextSize = 24.sp,
            titleTextSize = 14.sp,
            navigationIconSize = 24.dp,
            rowSpacing = 4.dp,
        )
        DatePickerSize.Large -> JalaliDatePickerSizes(
            containerPadding = 20.dp,
            dayCellSize = 52.dp,
            dayTouchTarget = 56.dp,
            dayTextSize = 17.sp,
            weekdayTextSize = 13.sp,
            headlineTextSize = 28.sp,
            titleTextSize = 16.sp,
            navigationIconSize = 28.dp,
            rowSpacing = 6.dp,
            dualMonthBreakpoint = 520.dp,
        )
    }
