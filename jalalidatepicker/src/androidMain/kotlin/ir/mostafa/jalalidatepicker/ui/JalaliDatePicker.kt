package ir.mostafa.jalalidatepicker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.mostafa.jalalidatepicker.calendar.CalendarDate
import ir.mostafa.jalalidatepicker.calendar.CalendarDateRange
import ir.mostafa.jalalidatepicker.calendar.CalendarEngine
import ir.mostafa.jalalidatepicker.calendar.CalendarType
import ir.mostafa.jalalidatepicker.locale.DateLabels
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.model.DigitMode
import ir.mostafa.jalalidatepicker.model.SelectionMode
import ir.mostafa.jalalidatepicker.state.JalaliDatePickerState

/**
 * Inline Material 3 Jalali / Gregorian date picker content.
 *
 * Use [JalaliDatePickerDialog] or [JalaliDatePickerBottomSheet] for modal presentations.
 */
@Composable
fun JalaliDatePicker(
    state: JalaliDatePickerState,
    modifier: Modifier = Modifier,
    title: String? = null,
    showModeToggle: Boolean = true,
    showActions: Boolean = false,
    confirmLabel: String = "تأیید",
    cancelLabel: String = "انصراف",
    usePersianLabels: Boolean = true,
    colors: JalaliDatePickerColors = JalaliDatePickerDefaults.colors(),
    sizes: JalaliDatePickerSizes = JalaliDatePickerDefaults.sizes(DatePickerSize.Medium),
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
) {
    val calendarType by state.calendarType.collectAsStateWithLifecycle()
    val selectionMode by state.selectionMode.collectAsStateWithLifecycle()
    val selectedDate by state.selectedDate.collectAsStateWithLifecycle()
    val selectedRange by state.selectedRange.collectAsStateWithLifecycle()
    val rangeStartPending by state.rangeStartPending.collectAsStateWithLifecycle()
    val displayedMonth by state.displayedMonth.collectAsStateWithLifecycle()
    val firstDayOfWeek by state.firstDayOfWeek.collectAsStateWithLifecycle()
    val digitMode by state.digitMode.collectAsStateWithLifecycle()

    val today = CalendarEngine.today(calendarType)
    val weekdays = DateLabels.orderedWeekdays(firstDayOfWeek)
    val grid = state.monthGrid()

    val headline = when (selectionMode) {
        SelectionMode.SINGLE -> selectedDate?.let {
            formatHeadline(it, calendarType, digitMode, usePersianLabels)
        } ?: defaultTitle(calendarType, usePersianLabels)

        SelectionMode.RANGE -> {
            val start = selectedRange?.start ?: rangeStartPending
            val end = selectedRange?.end
            when {
                start != null && end != null ->
                    "${formatHeadline(start, calendarType, digitMode, usePersianLabels)} – " +
                        formatHeadline(end, calendarType, digitMode, usePersianLabels)

                start != null -> formatHeadline(start, calendarType, digitMode, usePersianLabels)
                else -> if (usePersianLabels) "بازه تاریخ را انتخاب کنید" else "Select a date range"
            }
        }
    }

    Surface(
        modifier = modifier,
        color = colors.containerColor,
        shape = RoundedCornerShape(28.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(sizes.containerPadding)
                .fillMaxWidth(),
        ) {
            Text(
                text = title ?: defaultTitle(calendarType, usePersianLabels),
                color = colors.titleContentColor,
                fontSize = sizes.titleTextSize,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = headline,
                color = colors.headlineContentColor,
                fontSize = sizes.headlineTextSize,
                fontWeight = FontWeight.Normal,
            )

            if (showModeToggle) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = {
                            val next = if (calendarType == CalendarType.JALALI) {
                                CalendarType.GREGORIAN
                            } else {
                                CalendarType.JALALI
                            }
                            state.setCalendarType(next)
                            state.setDigitMode(
                                if (next == CalendarType.JALALI) DigitMode.PERSIAN else DigitMode.LATIN,
                            )
                        },
                    ) {
                        Text(
                            text = if (calendarType == CalendarType.JALALI) {
                                if (usePersianLabels) "میلادی" else "Gregorian"
                            } else {
                                if (usePersianLabels) "شمسی" else "Jalali"
                            },
                        )
                    }
                }
            }

            HorizontalDivider(color = colors.dividerColor)
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { state.navigateMonth(-1) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous month",
                        tint = colors.navigationContentColor,
                        modifier = Modifier.size(sizes.navigationIconSize),
                    )
                }
                Text(
                    text = buildString {
                        append(DateLabels.monthName(calendarType, displayedMonth.month, usePersianLabels))
                        append(' ')
                        append(DateLabels.formatNumber(displayedMonth.year, digitMode))
                    },
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = colors.headlineContentColor,
                    fontSize = sizes.titleTextSize,
                    fontWeight = FontWeight.Medium,
                )
                IconButton(onClick = { state.navigateMonth(1) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next month",
                        tint = colors.navigationContentColor,
                        modifier = Modifier.size(sizes.navigationIconSize),
                    )
                }
            }

            Spacer(Modifier.height(sizes.rowSpacing))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                weekdays.forEach { day ->
                    Text(
                        text = DateLabels.weekdayShort(day, usePersianLabels),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = colors.weekdayContentColor,
                        fontSize = sizes.weekdayTextSize,
                    )
                }
            }

            Spacer(Modifier.height(sizes.rowSpacing))

            grid.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    week.forEach { date ->
                        val role = dayRole(
                            date = date,
                            today = today,
                            selectionMode = selectionMode,
                            selectedDate = selectedDate,
                            selectedRange = selectedRange,
                            rangeStartPending = rangeStartPending,
                        )
                        DayCell(
                            date = date,
                            enabled = date != null && state.isSelectable(date),
                            role = role,
                            digitMode = digitMode,
                            colors = colors,
                            sizes = sizes,
                            onClick = state::select,
                        )
                    }
                }
                Spacer(Modifier.height(sizes.rowSpacing))
            }

            if (showActions) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = {
                            state.cancel()
                            onCancel?.invoke()
                        },
                    ) {
                        Text(cancelLabel)
                    }
                    TextButton(
                        onClick = {
                            state.confirm()
                            onConfirm?.invoke()
                        },
                    ) {
                        Text(confirmLabel)
                    }
                }
            }
        }
    }
}

private fun defaultTitle(type: CalendarType, persian: Boolean): String = when (type) {
    CalendarType.JALALI -> if (persian) "انتخاب تاریخ" else "Select date"
    CalendarType.GREGORIAN -> if (persian) "انتخاب تاریخ میلادی" else "Select Gregorian date"
}

private fun formatHeadline(
    date: CalendarDate,
    type: CalendarType,
    digitMode: DigitMode,
    persian: Boolean,
): String {
    val month = DateLabels.monthName(type, date.month, persian)
    val day = DateLabels.formatNumber(date.day, digitMode)
    val year = DateLabels.formatNumber(date.year, digitMode)
    return "$day $month $year"
}

private fun dayRole(
    date: CalendarDate?,
    today: CalendarDate,
    selectionMode: SelectionMode,
    selectedDate: CalendarDate?,
    selectedRange: CalendarDateRange?,
    rangeStartPending: CalendarDate?,
): DaySelectionRole {
    if (date == null) return DaySelectionRole.None
    return when (selectionMode) {
        SelectionMode.SINGLE -> when {
            date == selectedDate -> DaySelectionRole.Selected
            date == today -> DaySelectionRole.Today
            else -> DaySelectionRole.None
        }

        SelectionMode.RANGE -> {
            val range = selectedRange
            when {
                range != null && date == range.start && date == range.end -> DaySelectionRole.Selected
                range != null && date == range.start -> DaySelectionRole.RangeStart
                range != null && date == range.end -> DaySelectionRole.RangeEnd
                range != null && date in range -> DaySelectionRole.RangeMiddle
                rangeStartPending != null && date == rangeStartPending -> DaySelectionRole.RangeStart
                date == today -> DaySelectionRole.Today
                else -> DaySelectionRole.None
            }
        }
    }
}
