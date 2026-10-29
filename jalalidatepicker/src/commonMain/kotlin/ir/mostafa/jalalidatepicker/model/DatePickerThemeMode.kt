package ir.mostafa.jalalidatepicker.model

/**
 * How the picker resolves light / dark colors.
 *
 * The host app usually wraps content in [androidx.compose.material3.MaterialTheme];
 * use [FollowSystem] (default via MaterialTheme) or force [Light] / [Dark] color schemes
 * when building [ir.mostafa.jalalidatepicker.ui.JalaliDatePickerColors].
 */
enum class DatePickerThemeMode {
    Light,
    Dark,
    FollowSystem,
}
