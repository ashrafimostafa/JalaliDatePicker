package ir.mostafa.jalalidatepicker.ui.factory

import androidx.compose.runtime.Immutable
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerColors
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerSizes

/**
 * Full visual configuration for the picker — produced by a [JalaliDatePickerStyleFactory].
 */
@Immutable
data class JalaliDatePickerStyle(
    val colors: JalaliDatePickerColors,
    val sizes: JalaliDatePickerSizes,
    val typography: JalaliDatePickerTypography,
    val shapes: JalaliDatePickerShapes,
    val animations: JalaliDatePickerAnimations,
    val strings: JalaliDatePickerStrings,
    val layout: JalaliDatePickerLayoutConfig,
)
