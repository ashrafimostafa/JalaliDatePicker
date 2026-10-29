package ir.mostafa.jalalidatepicker.ui.factory

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.mostafa.jalalidatepicker.calendar.CalendarType
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerColors
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerSizes
import ir.mostafa.jalalidatepicker.ui.font.VazirFontFamily

/**
 * Abstract factory for every UI concern of the date picker.
 *
 * Implement this (or use [JalaliDatePickerStyleFactoryBuilder]) to customize
 * colors, typography, shapes, sizes, animations, strings, and layout/RTL.
 */
interface JalaliDatePickerStyleFactory {
    fun createColors(colorScheme: ColorScheme): JalaliDatePickerColors
    fun createSizes(size: DatePickerSize): JalaliDatePickerSizes
    fun createTypography(size: DatePickerSize): JalaliDatePickerTypography
    fun createShapes(): JalaliDatePickerShapes
    fun createAnimations(): JalaliDatePickerAnimations
    fun createStrings(usePersianLabels: Boolean): JalaliDatePickerStrings
    fun createLayout(
        calendarType: CalendarType,
        showModeToggle: Boolean,
        showActions: Boolean,
        usePersianLabels: Boolean,
    ): JalaliDatePickerLayoutConfig

    fun create(
        colorScheme: ColorScheme,
        calendarType: CalendarType = CalendarType.JALALI,
        size: DatePickerSize = DatePickerSize.Medium,
        showModeToggle: Boolean = true,
        showActions: Boolean = false,
        usePersianLabels: Boolean = true,
    ): JalaliDatePickerStyle = JalaliDatePickerStyle(
        colors = createColors(colorScheme),
        sizes = createSizes(size),
        typography = createTypography(size),
        shapes = createShapes(),
        animations = createAnimations(),
        strings = createStrings(usePersianLabels),
        layout = createLayout(calendarType, showModeToggle, showActions, usePersianLabels),
    )

    companion object {
        fun default(
            fontFamily: FontFamily = VazirFontFamily.FarsiDigits,
        ): JalaliDatePickerStyleFactory = DefaultJalaliDatePickerStyleFactory(fontFamily = fontFamily)

        fun builder(): JalaliDatePickerStyleFactoryBuilder = JalaliDatePickerStyleFactoryBuilder()
    }
}

/**
 * Default factory: Vazir (FD) typography, Material 3 colors, Jalali → RTL.
 */
class DefaultJalaliDatePickerStyleFactory(
    private val fontFamily: FontFamily = VazirFontFamily.FarsiDigits,
    private val forceLayoutDirection: LayoutDirection? = null,
) : JalaliDatePickerStyleFactory {

    override fun createColors(colorScheme: ColorScheme): JalaliDatePickerColors =
        JalaliDatePickerColors(
            containerColor = colorScheme.surfaceContainerHigh,
            titleContentColor = colorScheme.onSurfaceVariant,
            headlineContentColor = colorScheme.onSurface,
            weekdayContentColor = colorScheme.onSurfaceVariant,
            dayContentColor = colorScheme.onSurface,
            disabledDayContentColor = colorScheme.onSurface.copy(alpha = 0.38f),
            selectedDayContainerColor = colorScheme.primary,
            selectedDayContentColor = colorScheme.onPrimary,
            todayContentColor = colorScheme.primary,
            todayContainerColor = Color.Transparent,
            rangeSelectionContainerColor = colorScheme.primaryContainer.copy(alpha = 0.55f),
            navigationContentColor = colorScheme.onSurfaceVariant,
            dividerColor = colorScheme.outlineVariant,
            actionContentColor = colorScheme.primary,
            modeToggleContentColor = colorScheme.primary,
        )

    override fun createSizes(size: DatePickerSize): JalaliDatePickerSizes = when (size) {
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

    override fun createTypography(size: DatePickerSize): JalaliDatePickerTypography =
        JalaliDatePickerTypographyFactory.create(size = size, fontFamily = fontFamily)

    override fun createShapes(): JalaliDatePickerShapes =
        JalaliDatePickerShapesFactory.create()

    override fun createAnimations(): JalaliDatePickerAnimations =
        JalaliDatePickerAnimationsFactory.create()

    override fun createStrings(usePersianLabels: Boolean): JalaliDatePickerStrings =
        JalaliDatePickerStringsFactory.create(usePersianLabels)

    override fun createLayout(
        calendarType: CalendarType,
        showModeToggle: Boolean,
        showActions: Boolean,
        usePersianLabels: Boolean,
    ): JalaliDatePickerLayoutConfig = JalaliDatePickerLayoutFactory.create(
        calendarType = calendarType,
        showModeToggle = showModeToggle,
        showActions = showActions,
        usePersianLabels = usePersianLabels,
        forceDirection = forceLayoutDirection,
    )
}

/**
 * Fluent builder over [DefaultJalaliDatePickerStyleFactory] — override any product.
 */
class JalaliDatePickerStyleFactoryBuilder {
    private var fontFamily: FontFamily = VazirFontFamily.FarsiDigits
    private var forceLayoutDirection: LayoutDirection? = null
    private var colorsOverride: ((ColorScheme) -> JalaliDatePickerColors)? = null
    private var sizesOverride: ((DatePickerSize) -> JalaliDatePickerSizes)? = null
    private var typographyOverride: ((DatePickerSize, FontFamily) -> JalaliDatePickerTypography)? = null
    private var shapesOverride: (() -> JalaliDatePickerShapes)? = null
    private var animationsOverride: (() -> JalaliDatePickerAnimations)? = null
    private var stringsOverride: ((Boolean) -> JalaliDatePickerStrings)? = null
    private var layoutOverride: (
        (CalendarType, Boolean, Boolean, Boolean) -> JalaliDatePickerLayoutConfig
    )? = null
    private var containerCorner: Dp = 28.dp
    private var dayShape: Shape = CircleShape
    private var animationsEnabled: Boolean = true

    fun fontFamily(family: FontFamily) = apply { fontFamily = family }
    fun layoutDirection(direction: LayoutDirection?) = apply { forceLayoutDirection = direction }
    fun colors(block: (ColorScheme) -> JalaliDatePickerColors) = apply { colorsOverride = block }
    fun sizes(block: (DatePickerSize) -> JalaliDatePickerSizes) = apply { sizesOverride = block }
    fun typography(block: (DatePickerSize, FontFamily) -> JalaliDatePickerTypography) =
        apply { typographyOverride = block }
    fun shapes(block: () -> JalaliDatePickerShapes) = apply { shapesOverride = block }
    fun animations(block: () -> JalaliDatePickerAnimations) = apply { animationsOverride = block }
    fun strings(block: (Boolean) -> JalaliDatePickerStrings) = apply { stringsOverride = block }
    fun layout(
        block: (CalendarType, Boolean, Boolean, Boolean) -> JalaliDatePickerLayoutConfig,
    ) = apply { layoutOverride = block }

    fun containerCorner(dp: Dp) = apply { containerCorner = dp }
    fun dayShape(shape: Shape) = apply { dayShape = shape }
    fun animationsEnabled(enabled: Boolean) = apply { animationsEnabled = enabled }

    fun build(): JalaliDatePickerStyleFactory {
        val base = DefaultJalaliDatePickerStyleFactory(
            fontFamily = fontFamily,
            forceLayoutDirection = forceLayoutDirection,
        )
        val corner = containerCorner
        val day = dayShape
        val animOn = animationsEnabled
        return object : JalaliDatePickerStyleFactory by base {
            override fun createColors(colorScheme: ColorScheme): JalaliDatePickerColors =
                colorsOverride?.invoke(colorScheme) ?: base.createColors(colorScheme)

            override fun createSizes(size: DatePickerSize): JalaliDatePickerSizes =
                sizesOverride?.invoke(size) ?: base.createSizes(size)

            override fun createTypography(size: DatePickerSize): JalaliDatePickerTypography =
                typographyOverride?.invoke(size, fontFamily) ?: base.createTypography(size)

            override fun createShapes(): JalaliDatePickerShapes =
                shapesOverride?.invoke()
                    ?: JalaliDatePickerShapesFactory.create(
                        containerCorner = corner,
                        dayShape = day,
                    )

            override fun createAnimations(): JalaliDatePickerAnimations =
                animationsOverride?.invoke()
                    ?: JalaliDatePickerAnimationsFactory.create(enabled = animOn)

            override fun createStrings(usePersianLabels: Boolean): JalaliDatePickerStrings =
                stringsOverride?.invoke(usePersianLabels) ?: base.createStrings(usePersianLabels)

            override fun createLayout(
                calendarType: CalendarType,
                showModeToggle: Boolean,
                showActions: Boolean,
                usePersianLabels: Boolean,
            ): JalaliDatePickerLayoutConfig =
                layoutOverride?.invoke(calendarType, showModeToggle, showActions, usePersianLabels)
                    ?: base.createLayout(calendarType, showModeToggle, showActions, usePersianLabels)
        }
    }
}

@Composable
fun rememberJalaliDatePickerStyle(
    factory: JalaliDatePickerStyleFactory = JalaliDatePickerStyleFactory.default(),
    calendarType: CalendarType = CalendarType.JALALI,
    size: DatePickerSize = DatePickerSize.Medium,
    showModeToggle: Boolean = true,
    showActions: Boolean = false,
    usePersianLabels: Boolean = true,
): JalaliDatePickerStyle {
    val colorScheme = MaterialTheme.colorScheme
    return remember(
        factory,
        colorScheme,
        calendarType,
        size,
        showModeToggle,
        showActions,
        usePersianLabels,
    ) {
        factory.create(
            colorScheme = colorScheme,
            calendarType = calendarType,
            size = size,
            showModeToggle = showModeToggle,
            showActions = showActions,
            usePersianLabels = usePersianLabels,
        )
    }
}
