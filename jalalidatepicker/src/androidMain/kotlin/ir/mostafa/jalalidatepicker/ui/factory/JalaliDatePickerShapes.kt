package ir.mostafa.jalalidatepicker.ui.factory

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class JalaliDatePickerShapes(
    val container: Shape,
    val day: Shape,
    val todayBorderWidth: Dp,
    val rangeStart: Shape,
    val rangeEnd: Shape,
    val rangeMiddle: Shape,
)

object JalaliDatePickerShapesFactory {
    fun create(
        containerCorner: Dp = 28.dp,
        dayShape: Shape = CircleShape,
        todayBorderWidth: Dp = 1.5.dp,
    ): JalaliDatePickerShapes = JalaliDatePickerShapes(
        container = RoundedCornerShape(containerCorner),
        day = dayShape,
        todayBorderWidth = todayBorderWidth,
        // Start/End use layout-direction-aware corners (RTL-safe).
        rangeStart = RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp),
        rangeEnd = RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp),
        rangeMiddle = RoundedCornerShape(0.dp),
    )
}
