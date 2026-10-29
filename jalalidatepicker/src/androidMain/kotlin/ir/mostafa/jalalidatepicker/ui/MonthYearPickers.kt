package ir.mostafa.jalalidatepicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.mostafa.jalalidatepicker.calendar.CalendarType
import ir.mostafa.jalalidatepicker.calendar.YearMonth
import ir.mostafa.jalalidatepicker.locale.DateLabels
import ir.mostafa.jalalidatepicker.model.DigitMode
import ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerStyle

@Composable
internal fun MonthPickerGrid(
    calendarType: CalendarType,
    selectedMonth: Int,
    usePersian: Boolean,
    style: JalaliDatePickerStyle,
    onMonthSelected: (Int) -> Unit,
) {
    val months = remember { (1..12).toList() }
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .height(style.sizes.dayCellSize * 5),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        userScrollEnabled = false,
    ) {
        items(months, key = { it }) { month ->
            val selected = month == selectedMonth
            Box(
                modifier = Modifier
                    .aspectRatio(2.2f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (selected) {
                            style.colors.selectedDayContainerColor
                        } else {
                            style.colors.containerColor
                        },
                    )
                    .clickable(role = Role.Button) { onMonthSelected(month) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = DateLabels.monthName(calendarType, month, usePersian),
                    color = if (selected) {
                        style.colors.selectedDayContentColor
                    } else {
                        style.colors.dayContentColor
                    },
                    style = style.typography.monthLabel,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
internal fun YearPickerGrid(
    displayed: YearMonth,
    digitMode: DigitMode,
    style: JalaliDatePickerStyle,
    onYearSelected: (Int) -> Unit,
    onDecadeChange: (Int) -> Unit,
) {
    val decadeStart = remember(displayed.year) { displayed.year - ((displayed.year % 10) + 10) % 10 }
    val years = remember(decadeStart) { (decadeStart until decadeStart + 12).toList() }
    val decadeEnd = decadeStart + 9

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { onDecadeChange(-10) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = style.strings.previousDecade,
                    tint = style.colors.navigationContentColor,
                    modifier = Modifier.size(style.sizes.navigationIconSize),
                )
            }
            Text(
                text = buildString {
                    append(DateLabels.formatNumber(decadeStart, digitMode))
                    append(" – ")
                    append(DateLabels.formatNumber(decadeEnd, digitMode))
                },
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = style.colors.headlineContentColor,
                style = style.typography.monthLabel,
            )
            IconButton(onClick = { onDecadeChange(10) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = style.strings.nextDecade,
                    tint = style.colors.navigationContentColor,
                    modifier = Modifier.size(style.sizes.navigationIconSize),
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(style.sizes.dayCellSize * 5),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            userScrollEnabled = false,
        ) {
            items(years, key = { it }) { year ->
                val selected = year == displayed.year
                val inDecade = year in decadeStart..decadeEnd
                Box(
                    modifier = Modifier
                        .aspectRatio(2.2f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                selected -> style.colors.selectedDayContainerColor
                                else -> style.colors.containerColor
                            },
                        )
                        .clickable(role = Role.Button) { onYearSelected(year) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = DateLabels.formatNumber(year, digitMode),
                        color = when {
                            selected -> style.colors.selectedDayContentColor
                            inDecade -> style.colors.dayContentColor
                            else -> style.colors.disabledDayContentColor
                        },
                        style = style.typography.monthLabel,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
