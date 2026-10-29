package ir.mostafa.jalalidatepicker.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role
import ir.mostafa.jalalidatepicker.calendar.CalendarDate
import ir.mostafa.jalalidatepicker.locale.DateLabels
import ir.mostafa.jalalidatepicker.model.DigitMode
import ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerAnimations
import ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerShapes
import ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerTypography

internal enum class DaySelectionRole {
    None,
    Selected,
    RangeStart,
    RangeEnd,
    RangeMiddle,
    Today,
}

@Composable
internal fun DayCell(
    date: CalendarDate?,
    enabled: Boolean,
    role: DaySelectionRole,
    digitMode: DigitMode,
    colors: JalaliDatePickerColors,
    sizes: JalaliDatePickerSizes,
    typography: JalaliDatePickerTypography,
    shapes: JalaliDatePickerShapes,
    animations: JalaliDatePickerAnimations,
    onClick: (CalendarDate) -> Unit,
) {
    if (date == null) {
        Box(modifier = Modifier.size(sizes.dayCellSize))
        return
    }

    val isEndpoint = role == DaySelectionRole.Selected ||
        role == DaySelectionRole.RangeStart ||
        role == DaySelectionRole.RangeEnd
    val inRangeBand = role == DaySelectionRole.RangeMiddle ||
        role == DaySelectionRole.RangeStart ||
        role == DaySelectionRole.RangeEnd

    val rangeShape = when (role) {
        DaySelectionRole.RangeStart -> shapes.rangeStart
        DaySelectionRole.RangeEnd -> shapes.rangeEnd
        DaySelectionRole.RangeMiddle -> shapes.rangeMiddle
        else -> RectangleShape
    }

    val targetContainer = when {
        isEndpoint -> colors.selectedDayContainerColor
        role == DaySelectionRole.Today -> colors.todayContainerColor
        else -> Color.Transparent
    }
    val targetContent = when {
        !enabled -> colors.disabledDayContentColor
        isEndpoint -> colors.selectedDayContentColor
        role == DaySelectionRole.Today -> colors.todayContentColor
        else -> colors.dayContentColor
    }

    val containerColor by animateColorAsState(
        targetValue = targetContainer,
        animationSpec = if (animations.enabled) animations.colorSpec else snap(),
        label = "dayContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = targetContent,
        animationSpec = if (animations.enabled) animations.colorSpec else snap(),
        label = "dayContent",
    )
    val scale by animateFloatAsState(
        targetValue = if (isEndpoint && animations.enabled) 1.06f else 1f,
        animationSpec = if (animations.enabled) {
            animations.selectionSpec
        } else {
            snap()
        },
        label = "dayScale",
    )

    Box(
        modifier = Modifier.size(sizes.dayCellSize),
        contentAlignment = Alignment.Center,
    ) {
        if (inRangeBand && role != DaySelectionRole.Selected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(rangeShape)
                    .background(colors.rangeSelectionContainerColor),
            )
        }
        Box(
            modifier = Modifier
                .size(sizes.dayCellSize * 0.9f)
                .scale(scale)
                .clip(shapes.day)
                .background(containerColor)
                .then(
                    if (role == DaySelectionRole.Today && !isEndpoint) {
                        Modifier.border(
                            width = shapes.todayBorderWidth,
                            color = colors.todayContentColor,
                            shape = shapes.day,
                        )
                    } else {
                        Modifier
                    },
                )
                .clickable(
                    enabled = enabled,
                    role = Role.Button,
                    onClick = { onClick(date) },
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = DateLabels.formatNumber(date.day, digitMode),
                color = contentColor,
                style = if (isEndpoint || role == DaySelectionRole.Today) {
                    typography.daySelected
                } else {
                    typography.day
                },
            )
        }
    }
}
