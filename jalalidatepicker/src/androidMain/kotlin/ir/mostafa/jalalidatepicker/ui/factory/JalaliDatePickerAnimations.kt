package ir.mostafa.jalalidatepicker.ui.factory

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset

@Immutable
data class JalaliDatePickerAnimations(
    val enabled: Boolean,
    val monthChangeMillis: Int,
    val selectionSpec: FiniteAnimationSpec<Float>,
    val colorSpec: FiniteAnimationSpec<Color>,
    val monthSlideSpec: FiniteAnimationSpec<IntOffset>,
    val contentFadeMillis: Int,
)

object JalaliDatePickerAnimationsFactory {
    fun create(
        enabled: Boolean = true,
        monthChangeMillis: Int = 320,
        contentFadeMillis: Int = 180,
    ): JalaliDatePickerAnimations = JalaliDatePickerAnimations(
        enabled = enabled,
        monthChangeMillis = monthChangeMillis,
        selectionSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        colorSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
        monthSlideSpec = tween(durationMillis = monthChangeMillis, easing = FastOutSlowInEasing),
        contentFadeMillis = contentFadeMillis,
    )
}
