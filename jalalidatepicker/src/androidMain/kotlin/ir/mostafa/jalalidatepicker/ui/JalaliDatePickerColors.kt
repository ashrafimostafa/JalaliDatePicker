package ir.mostafa.jalalidatepicker.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import ir.mostafa.jalalidatepicker.model.DatePickerThemeMode

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
    val actionContentColor: Color = Color.Unspecified,
    val modeToggleContentColor: Color = Color.Unspecified,
    val hintContentColor: Color = Color.Unspecified,
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
        todayContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
        rangeSelectionContainerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
        navigationContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        dividerColor: Color = MaterialTheme.colorScheme.outlineVariant,
        actionContentColor: Color = MaterialTheme.colorScheme.primary,
        modeToggleContentColor: Color = MaterialTheme.colorScheme.primary,
        hintContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    ): JalaliDatePickerColors = fromColorScheme(
        colorScheme = MaterialTheme.colorScheme,
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
        actionContentColor = actionContentColor,
        modeToggleContentColor = modeToggleContentColor,
        hintContentColor = hintContentColor,
    )

    /**
     * Build colors from an explicit [ColorScheme] (useful in style factories).
     */
    fun fromColorScheme(
        colorScheme: ColorScheme,
        containerColor: Color = colorScheme.surfaceContainerHigh,
        titleContentColor: Color = colorScheme.onSurfaceVariant,
        headlineContentColor: Color = colorScheme.onSurface,
        weekdayContentColor: Color = colorScheme.onSurfaceVariant,
        dayContentColor: Color = colorScheme.onSurface,
        disabledDayContentColor: Color = colorScheme.onSurface.copy(alpha = 0.38f),
        selectedDayContainerColor: Color = colorScheme.primary,
        selectedDayContentColor: Color = colorScheme.onPrimary,
        todayContentColor: Color = colorScheme.primary,
        todayContainerColor: Color = colorScheme.primaryContainer.copy(alpha = 0.55f),
        rangeSelectionContainerColor: Color = colorScheme.primaryContainer.copy(alpha = 0.55f),
        navigationContentColor: Color = colorScheme.onSurfaceVariant,
        dividerColor: Color = colorScheme.outlineVariant,
        actionContentColor: Color = colorScheme.primary,
        modeToggleContentColor: Color = colorScheme.primary,
        hintContentColor: Color = colorScheme.onSurfaceVariant,
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
        actionContentColor = actionContentColor,
        modeToggleContentColor = modeToggleContentColor,
        hintContentColor = hintContentColor,
    )

    /**
     * Resolve a [ColorScheme] for [themeMode], then map to picker colors.
     * Optional [primary] overrides the brand accent.
     */
    @Composable
    fun colorsForTheme(
        themeMode: DatePickerThemeMode = DatePickerThemeMode.FollowSystem,
        primary: Color? = null,
        configure: (JalaliDatePickerColors) -> JalaliDatePickerColors = { it },
    ): JalaliDatePickerColors {
        val dark = when (themeMode) {
            DatePickerThemeMode.Light -> false
            DatePickerThemeMode.Dark -> true
            DatePickerThemeMode.FollowSystem -> isSystemInDarkTheme()
        }
        val base = if (dark) {
            if (primary != null) darkColorScheme(primary = primary) else darkColorScheme()
        } else {
            if (primary != null) lightColorScheme(primary = primary) else lightColorScheme()
        }
        return configure(fromColorScheme(base))
    }
}
