package ir.mostafa.jalalidatepicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.mostafa.jalalidatepicker.calendar.CalendarDate
import ir.mostafa.jalalidatepicker.locale.DateLabels
import ir.mostafa.jalalidatepicker.model.DigitMode

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
        DaySelectionRole.RangeStart -> RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp)
        DaySelectionRole.RangeEnd -> RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp)
        DaySelectionRole.RangeMiddle -> RectangleShape
        else -> RectangleShape
    }

    val contentColor = when {
        !enabled -> colors.disabledDayContentColor
        isEndpoint -> colors.selectedDayContentColor
        role == DaySelectionRole.Today -> colors.todayContentColor
        else -> colors.dayContentColor
    }

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
                .size(sizes.dayCellSize - 4.dp)
                .clip(CircleShape)
                .then(
                    if (isEndpoint) {
                        Modifier.background(colors.selectedDayContainerColor)
                    } else if (role == DaySelectionRole.Today) {
                        Modifier
                            .background(colors.todayContainerColor)
                            .border(1.dp, colors.todayContentColor, CircleShape)
                    } else {
                        Modifier.background(Color.Transparent)
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
                fontSize = sizes.dayTextSize,
                fontWeight = if (isEndpoint || role == DaySelectionRole.Today) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
            )
        }
    }
}
