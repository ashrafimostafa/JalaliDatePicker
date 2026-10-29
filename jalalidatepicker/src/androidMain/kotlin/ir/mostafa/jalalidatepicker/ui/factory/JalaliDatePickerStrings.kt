package ir.mostafa.jalalidatepicker.ui.factory

import androidx.compose.runtime.Immutable

@Immutable
data class JalaliDatePickerStrings(
    val titleJalali: String,
    val titleGregorian: String,
    val rangeHint: String,
    val confirm: String,
    val cancel: String,
    val switchToGregorian: String,
    val switchToJalali: String,
    val previousMonth: String,
    val nextMonth: String,
    val today: String,
    val previousDecade: String,
    val nextDecade: String,
)

object JalaliDatePickerStringsFactory {
    fun create(usePersian: Boolean = true): JalaliDatePickerStrings =
        if (usePersian) {
            JalaliDatePickerStrings(
                titleJalali = "انتخاب تاریخ",
                titleGregorian = "انتخاب تاریخ میلادی",
                rangeHint = "بازه تاریخ را انتخاب کنید",
                confirm = "تأیید",
                cancel = "انصراف",
                switchToGregorian = "میلادی",
                switchToJalali = "شمسی",
                previousMonth = "ماه قبل",
                nextMonth = "ماه بعد",
                today = "امروز",
                previousDecade = "دهه قبل",
                nextDecade = "دهه بعد",
            )
        } else {
            JalaliDatePickerStrings(
                titleJalali = "Select date",
                titleGregorian = "Select Gregorian date",
                rangeHint = "Select a date range",
                confirm = "OK",
                cancel = "Cancel",
                switchToGregorian = "Gregorian",
                switchToJalali = "Jalali",
                previousMonth = "Previous month",
                nextMonth = "Next month",
                today = "Today",
                previousDecade = "Previous decade",
                nextDecade = "Next decade",
            )
        }
}
