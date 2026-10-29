package ir.mostafa.jalalidatepicker.ui.factory

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.ui.font.VazirFontFamily

@Immutable
data class JalaliDatePickerTypography(
    val fontFamily: FontFamily,
    val title: TextStyle,
    val headline: TextStyle,
    val monthLabel: TextStyle,
    val weekday: TextStyle,
    val day: TextStyle,
    val daySelected: TextStyle,
    val action: TextStyle,
    val modeToggle: TextStyle,
)

object JalaliDatePickerTypographyFactory {
    fun create(
        size: DatePickerSize = DatePickerSize.Medium,
        fontFamily: FontFamily = VazirFontFamily.FarsiDigits,
    ): JalaliDatePickerTypography {
        val titleSp: Int
        val headlineSp: Int
        val monthSp: Int
        val weekdaySp: Int
        val daySp: Int
        val actionSp: Int
        when (size) {
            DatePickerSize.Compact -> {
                titleSp = 12; headlineSp = 20; monthSp = 14
                weekdaySp = 11; daySp = 13; actionSp = 13
            }
            DatePickerSize.Medium -> {
                titleSp = 14; headlineSp = 24; monthSp = 16
                weekdaySp = 12; daySp = 14; actionSp = 14
            }
            DatePickerSize.Large -> {
                titleSp = 16; headlineSp = 32; monthSp = 18
                weekdaySp = 14; daySp = 18; actionSp = 16
            }
        }
        return JalaliDatePickerTypography(
            fontFamily = fontFamily,
            title = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = titleSp.sp,
                lineHeight = (titleSp + 4).sp,
            ),
            headline = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = headlineSp.sp,
                lineHeight = (headlineSp + 6).sp,
            ),
            monthLabel = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = monthSp.sp,
            ),
            weekday = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = weekdaySp.sp,
            ),
            day = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = daySp.sp,
            ),
            daySelected = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = daySp.sp,
            ),
            action = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = actionSp.sp,
            ),
            modeToggle = TextStyle(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = actionSp.sp,
            ),
        )
    }
}
