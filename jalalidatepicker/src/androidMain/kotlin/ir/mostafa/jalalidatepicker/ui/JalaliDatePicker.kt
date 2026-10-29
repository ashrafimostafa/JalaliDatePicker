package ir.mostafa.jalalidatepicker.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.mostafa.jalalidatepicker.calendar.CalendarDate
import ir.mostafa.jalalidatepicker.calendar.CalendarDateRange
import ir.mostafa.jalalidatepicker.calendar.CalendarEngine
import ir.mostafa.jalalidatepicker.calendar.CalendarType
import ir.mostafa.jalalidatepicker.calendar.YearMonth
import ir.mostafa.jalalidatepicker.locale.DateLabels
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.model.DigitMode
import ir.mostafa.jalalidatepicker.model.MonthCountMode
import ir.mostafa.jalalidatepicker.model.SelectionMode
import ir.mostafa.jalalidatepicker.state.JalaliDatePickerState
import ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerStyle
import ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerStyleFactory
import ir.mostafa.jalalidatepicker.ui.factory.rememberJalaliDatePickerStyle
import kotlin.math.abs

/**
 * Inline Material 3 Jalali / Gregorian date picker.
 *
 * Calendar type (Jalali vs Gregorian) is set only in code via
 * [JalaliDatePickerState] — there is no in-UI calendar switch.
 */
@Composable
fun JalaliDatePicker(
    state: JalaliDatePickerState,
    modifier: Modifier = Modifier,
    title: String? = null,
    size: DatePickerSize = DatePickerSize.Medium,
    styleFactory: JalaliDatePickerStyleFactory = JalaliDatePickerStyleFactory.default(),
    showActions: Boolean = false,
    usePersianLabels: Boolean = true,
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
) {
    val calendarType by state.calendarType.collectAsStateWithLifecycle()
    val style = rememberJalaliDatePickerStyle(
        factory = styleFactory,
        calendarType = calendarType,
        size = size,
        showActions = showActions,
        usePersianLabels = usePersianLabels,
    )
    JalaliDatePicker(
        state = state,
        style = style,
        modifier = modifier,
        title = title,
        onConfirm = onConfirm,
        onCancel = onCancel,
    )
}

@Composable
fun JalaliDatePicker(
    state: JalaliDatePickerState,
    style: JalaliDatePickerStyle,
    modifier: Modifier = Modifier,
    title: String? = null,
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

    val colors = style.colors
    val sizes = style.sizes
    val typography = style.typography
    val animations = style.animations
    val strings = style.strings
    val layout = style.layout

    val today = remember(calendarType) { CalendarEngine.today(calendarType) }
    val weekdays = remember(firstDayOfWeek) { DateLabels.orderedWeekdays(firstDayOfWeek) }
    val usePersian = layout.usePersianLabels

    var monthNavDirection by remember { mutableIntStateOf(0) }
    var displayMode by remember { mutableStateOf(PickerDisplayMode.Days) }
    var dragAccum by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val swipeThresholdPx = with(density) { 48.dp.toPx() }

    val selectionHint = when (selectionMode) {
        SelectionMode.SINGLE -> null
        SelectionMode.RANGE -> when {
            selectedRange != null -> null
            rangeStartPending == null -> strings.rangeStartHint
            else -> strings.rangeEndHint
        }
    }

    val headline = when (selectionMode) {
        SelectionMode.SINGLE -> selectedDate?.let {
            formatHeadline(it, calendarType, digitMode, usePersian)
        } ?: (title ?: defaultTitle(calendarType, strings))

        SelectionMode.RANGE -> {
            val start = selectedRange?.start ?: rangeStartPending
            val end = selectedRange?.end
            when {
                start != null && end != null ->
                    "${formatHeadline(start, calendarType, digitMode, usePersian)} – " +
                        formatHeadline(end, calendarType, digitMode, usePersian)

                start != null -> formatHeadline(start, calendarType, digitMode, usePersian)
                else -> strings.rangeHint
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides layout.layoutDirection) {
        Surface(
            modifier = modifier,
            color = colors.containerColor,
            shape = style.shapes.container,
        ) {
            BoxWithConstraints {
                val monthCount = resolveMonthCount(
                    mode = layout.monthCountMode,
                    maxWidth = maxWidth,
                    breakpoint = sizes.dualMonthBreakpoint,
                )
                val secondMonth = remember(displayedMonth) { displayedMonth.plusMonths(1) }

                Column(
                    modifier = Modifier
                        .padding(sizes.containerPadding)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = title ?: defaultTitle(calendarType, strings),
                        color = colors.titleContentColor,
                        style = typography.title,
                    )
                    Spacer(Modifier.height(sizes.rowSpacing))
                    Text(
                        text = headline,
                        color = colors.headlineContentColor,
                        style = typography.headline,
                    )
                    if (selectionHint != null) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = selectionHint,
                            color = colors.hintContentColor.takeUnless { it == Color.Unspecified }
                                ?: colors.titleContentColor,
                            style = typography.action,
                        )
                    }

                    if (layout.showTodayButton) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            TextButton(
                                onClick = {
                                    displayMode = PickerDisplayMode.Days
                                    state.goToToday(alsoSelect = selectionMode == SelectionMode.SINGLE)
                                },
                            ) {
                                Text(
                                    text = strings.today,
                                    color = colors.todayContentColor,
                                    style = typography.action,
                                )
                            }
                        }
                    }

                    if (layout.showDivider) {
                        HorizontalDivider(color = colors.dividerColor)
                        Spacer(Modifier.height(8.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            onClick = {
                                when (displayMode) {
                                    PickerDisplayMode.Days -> {
                                        monthNavDirection = -1
                                        state.navigateMonth(-1)
                                    }
                                    PickerDisplayMode.Months -> Unit
                                    PickerDisplayMode.Years -> state.navigateYear(-10)
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = when (displayMode) {
                                    PickerDisplayMode.Years -> strings.previousDecade
                                    else -> strings.previousMonth
                                },
                                tint = colors.navigationContentColor,
                                modifier = Modifier.size(sizes.navigationIconSize),
                            )
                        }

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = DateLabels.monthName(
                                    calendarType,
                                    displayedMonth.month,
                                    usePersian,
                                ),
                                modifier = Modifier
                                    .clickable(role = Role.Button) {
                                        displayMode = if (displayMode == PickerDisplayMode.Months) {
                                            PickerDisplayMode.Days
                                        } else {
                                            PickerDisplayMode.Months
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                color = if (displayMode == PickerDisplayMode.Months) {
                                    colors.selectedDayContainerColor
                                } else {
                                    colors.headlineContentColor
                                },
                                style = typography.monthLabel,
                            )
                            if (monthCount > 1 && displayMode == PickerDisplayMode.Days) {
                                Text(
                                    text = " / " + DateLabels.monthName(
                                        calendarType,
                                        secondMonth.month,
                                        usePersian,
                                    ),
                                    color = colors.titleContentColor,
                                    style = typography.monthLabel,
                                )
                            }
                            Text(
                                text = DateLabels.formatNumber(displayedMonth.year, digitMode),
                                modifier = Modifier
                                    .clickable(role = Role.Button) {
                                        displayMode = if (displayMode == PickerDisplayMode.Years) {
                                            PickerDisplayMode.Days
                                        } else {
                                            PickerDisplayMode.Years
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                color = if (displayMode == PickerDisplayMode.Years) {
                                    colors.selectedDayContainerColor
                                } else {
                                    colors.headlineContentColor
                                },
                                style = typography.monthLabel,
                            )
                        }

                        IconButton(
                            onClick = {
                                when (displayMode) {
                                    PickerDisplayMode.Days -> {
                                        monthNavDirection = 1
                                        state.navigateMonth(1)
                                    }
                                    PickerDisplayMode.Months -> Unit
                                    PickerDisplayMode.Years -> state.navigateYear(10)
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = when (displayMode) {
                                    PickerDisplayMode.Years -> strings.nextDecade
                                    else -> strings.nextMonth
                                },
                                tint = colors.navigationContentColor,
                                modifier = Modifier.size(sizes.navigationIconSize),
                            )
                        }
                    }

                    Spacer(Modifier.height(sizes.rowSpacing))

                    when (displayMode) {
                        PickerDisplayMode.Months -> {
                            MonthPickerGrid(
                                calendarType = calendarType,
                                selectedMonth = displayedMonth.month,
                                usePersian = usePersian,
                                style = style,
                                onMonthSelected = { month ->
                                    state.setMonth(month)
                                    displayMode = PickerDisplayMode.Days
                                },
                            )
                        }

                        PickerDisplayMode.Years -> {
                            YearPickerGrid(
                                displayed = displayedMonth,
                                digitMode = digitMode,
                                style = style,
                                onYearSelected = { year ->
                                    state.setYear(year)
                                    displayMode = PickerDisplayMode.Months
                                },
                                onDecadeChange = { delta -> state.navigateYear(delta) },
                            )
                        }

                        PickerDisplayMode.Days -> {
                            val isRtl = layout.layoutDirection == LayoutDirection.Rtl
                            val swipeModifier = if (layout.enableMonthSwipe) {
                                Modifier.pointerInput(displayedMonth, isRtl) {
                                    detectHorizontalDragGestures(
                                        onDragStart = { dragAccum = 0f },
                                        onDragEnd = {
                                            if (abs(dragAccum) >= swipeThresholdPx) {
                                                val goNext = if (isRtl) dragAccum > 0 else dragAccum < 0
                                                monthNavDirection = if (goNext) 1 else -1
                                                state.navigateMonth(if (goNext) 1 else -1)
                                            }
                                            dragAccum = 0f
                                        },
                                        onDragCancel = { dragAccum = 0f },
                                        onHorizontalDrag = { change, amount ->
                                            change.consume()
                                            dragAccum += amount
                                        },
                                    )
                                }
                            } else {
                                Modifier
                            }

                            val months = if (monthCount > 1) {
                                listOf(displayedMonth, secondMonth)
                            } else {
                                listOf(displayedMonth)
                            }

                            if (animations.enabled) {
                                AnimatedContent(
                                    targetState = displayedMonth,
                                    modifier = swipeModifier.fillMaxWidth(),
                                    transitionSpec = {
                                        val forward = monthNavDirection >= 0
                                        (
                                            fadeIn(tween(animations.contentFadeMillis)) +
                                                slideInHorizontally(
                                                    animationSpec = animations.monthSlideSpec,
                                                    initialOffsetX = { full ->
                                                        if (forward) full / 5 else -full / 5
                                                    },
                                                )
                                            ) togetherWith (
                                            fadeOut(tween(animations.contentFadeMillis)) +
                                                slideOutHorizontally(
                                                    animationSpec = animations.monthSlideSpec,
                                                    targetOffsetX = { full ->
                                                        if (forward) -full / 5 else full / 5
                                                    },
                                                )
                                            ) using SizeTransform(clip = false)
                                    },
                                    label = "monthGrid",
                                ) { baseMonth ->
                                    val panes = if (monthCount > 1) {
                                        listOf(baseMonth, baseMonth.plusMonths(1))
                                    } else {
                                        listOf(baseMonth)
                                    }
                                    MonthPanesRow(
                                        months = panes,
                                        calendarType = calendarType,
                                        weekdays = weekdays,
                                        usePersian = usePersian,
                                        today = today,
                                        selectionMode = selectionMode,
                                        selectedDate = selectedDate,
                                        selectedRange = selectedRange,
                                        rangeStartPending = rangeStartPending,
                                        digitMode = digitMode,
                                        firstDayOfWeekKey = firstDayOfWeek,
                                        style = style,
                                        state = state,
                                    )
                                }
                            } else {
                                MonthPanesRow(
                                    modifier = swipeModifier,
                                    months = months,
                                    calendarType = calendarType,
                                    weekdays = weekdays,
                                    usePersian = usePersian,
                                    today = today,
                                    selectionMode = selectionMode,
                                    selectedDate = selectedDate,
                                    selectedRange = selectedRange,
                                    rangeStartPending = rangeStartPending,
                                    digitMode = digitMode,
                                    firstDayOfWeekKey = firstDayOfWeek,
                                    style = style,
                                    state = state,
                                )
                            }
                        }
                    }

                    if (layout.showActions) {
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
                                Text(
                                    text = strings.cancel,
                                    color = colors.actionContentColor.takeUnless {
                                        it == Color.Unspecified
                                    } ?: colors.selectedDayContainerColor,
                                    style = typography.action,
                                )
                            }
                            TextButton(
                                onClick = {
                                    state.confirm()
                                    onConfirm?.invoke()
                                },
                            ) {
                                Text(
                                    text = strings.confirm,
                                    color = colors.actionContentColor.takeUnless {
                                        it == Color.Unspecified
                                    } ?: colors.selectedDayContainerColor,
                                    style = typography.action,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthPanesRow(
    months: List<YearMonth>,
    calendarType: CalendarType,
    weekdays: List<ir.mostafa.jalalidatepicker.calendar.DayOfWeek>,
    usePersian: Boolean,
    today: CalendarDate,
    selectionMode: SelectionMode,
    selectedDate: CalendarDate?,
    selectedRange: CalendarDateRange?,
    rangeStartPending: CalendarDate?,
    digitMode: DigitMode,
    firstDayOfWeekKey: ir.mostafa.jalalidatepicker.calendar.DayOfWeek,
    style: JalaliDatePickerStyle,
    state: JalaliDatePickerState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        months.forEachIndexed { index, month ->
            Column(modifier = Modifier.weight(1f)) {
                if (months.size > 1) {
                    Text(
                        text = buildString {
                            append(DateLabels.monthName(calendarType, month.month, usePersian))
                            append(' ')
                            append(DateLabels.formatNumber(month.year, digitMode))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = style.colors.titleContentColor,
                        style = style.typography.weekday,
                    )
                    Spacer(Modifier.height(4.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    weekdays.forEach { day ->
                        Text(
                            text = DateLabels.weekdayShort(day, usePersian),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            color = style.colors.weekdayContentColor,
                            style = style.typography.weekday,
                        )
                    }
                }
                Spacer(Modifier.height(style.sizes.rowSpacing))
                val grid = remember(month, firstDayOfWeekKey, calendarType) {
                    state.monthGrid(month)
                }
                MonthGrid(
                    grid = grid,
                    today = today,
                    selectionMode = selectionMode,
                    selectedDate = selectedDate,
                    selectedRange = selectedRange,
                    rangeStartPending = rangeStartPending,
                    digitMode = digitMode,
                    style = style,
                    onSelect = state::select,
                    isSelectable = state::isSelectable,
                )
            }
            if (index < months.lastIndex) {
                Spacer(Modifier.width(0.dp))
            }
        }
    }
}

@Composable
private fun MonthGrid(
    grid: List<CalendarDate?>,
    today: CalendarDate,
    selectionMode: SelectionMode,
    selectedDate: CalendarDate?,
    selectedRange: CalendarDateRange?,
    rangeStartPending: CalendarDate?,
    digitMode: DigitMode,
    style: JalaliDatePickerStyle,
    onSelect: (CalendarDate) -> Unit,
    isSelectable: (CalendarDate) -> Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        grid.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                week.forEach { date ->
                    DayCell(
                        date = date,
                        enabled = date != null && isSelectable(date),
                        role = dayRole(
                            date = date,
                            today = today,
                            selectionMode = selectionMode,
                            selectedDate = selectedDate,
                            selectedRange = selectedRange,
                            rangeStartPending = rangeStartPending,
                        ),
                        isToday = date == today,
                        digitMode = digitMode,
                        colors = style.colors,
                        sizes = style.sizes,
                        typography = style.typography,
                        shapes = style.shapes,
                        animations = style.animations,
                        onClick = onSelect,
                    )
                }
            }
            Spacer(Modifier.height(style.sizes.rowSpacing))
        }
    }
}

private fun resolveMonthCount(
    mode: MonthCountMode,
    maxWidth: androidx.compose.ui.unit.Dp,
    breakpoint: androidx.compose.ui.unit.Dp,
): Int = when (mode) {
    MonthCountMode.One -> 1
    MonthCountMode.Two -> 2
    MonthCountMode.Auto -> if (maxWidth >= breakpoint) 2 else 1
}

private fun defaultTitle(
    type: CalendarType,
    strings: ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerStrings,
): String = when (type) {
    CalendarType.JALALI -> strings.titleJalali
    CalendarType.GREGORIAN -> strings.titleGregorian
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
