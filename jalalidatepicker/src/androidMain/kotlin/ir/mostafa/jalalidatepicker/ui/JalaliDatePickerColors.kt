package ir.mostafa.jalalidatepicker.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class JalaliDatePickerColors(
    val containerColor: Color,
    val titleContentColor: Color,
    val headlineContentColor: Color,
    val weekdayContentColor: Color,
    val dayContentColor: Color,
    val disabledDayContentColor: Color,
    val selectedDayContainerColor: Color,
    val selectedDayContentColor: Color,
    val todayContentColor: Color,
    val todayContainerColor: Color,
    val rangeSelectionContainerColor: Color,
    val navigationContentColor: Color,
    val dividerColor: Color,
)

object JalaliDatePickerDefaults {

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
        titleContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        headlineContentColor: Color = MaterialTheme.colorScheme.onSurface,
        weekdayContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        dayContentColor: Color = MaterialTheme.colorScheme.onSurface,
        disabledDayContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        selectedDayContainerColor: Color = MaterialTheme.colorScheme.primary,
        selectedDayContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        todayContentColor: Color = MaterialTheme.colorScheme.primary,
        todayContainerColor: Color = Color.Transparent,
        rangeSelectionContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
        navigationContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        dividerColor: Color = MaterialTheme.colorScheme.outlineVariant,
    ): JalaliDatePickerColors = JalaliDatePickerColors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        headlineContentColor = headlineContentColor,
        weekdayContentColor = weekdayContentColor,
        dayContentColor = dayContentColor,
        disabledDayContentColor = disabledDayContentColor,
        selectedDayContainerColor = selectedDayContainerColor,
        selectedDayContentColor = selectedDayContentColor,
        todayContentColor = todayContentColor,
        todayContainerColor = todayContainerColor,
        rangeSelectionContainerColor = rangeSelectionContainerColor,
        navigationContentColor = navigationContentColor,
        dividerColor = dividerColor,
    )
}
