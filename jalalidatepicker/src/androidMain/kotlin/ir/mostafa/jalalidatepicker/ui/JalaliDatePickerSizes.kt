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
    val dayTextSize: TextUnit,
    val weekdayTextSize: TextUnit,
    val headlineTextSize: TextUnit,
    val titleTextSize: TextUnit,
    val navigationIconSize: Dp,
    val rowSpacing: Dp,
)

fun JalaliDatePickerDefaults.sizes(size: DatePickerSize = DatePickerSize.Medium): JalaliDatePickerSizes =
    when (size) {
        DatePickerSize.Compact -> JalaliDatePickerSizes(
            containerPadding = 12.dp,
            dayCellSize = 36.dp,
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
            dayTextSize = 14.sp,
            weekdayTextSize = 12.sp,
            headlineTextSize = 24.sp,
            titleTextSize = 14.sp,
            navigationIconSize = 24.dp,
            rowSpacing = 4.dp,
        )
        DatePickerSize.Large -> JalaliDatePickerSizes(
            containerPadding = 20.dp,
            dayCellSize = 56.dp,
            dayTextSize = 18.sp,
            weekdayTextSize = 14.sp,
            headlineTextSize = 32.sp,
            titleTextSize = 16.sp,
            navigationIconSize = 28.dp,
            rowSpacing = 6.dp,
        )
    }
