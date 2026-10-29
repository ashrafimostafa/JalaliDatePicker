# Jalali DatePicker

Highly customizable **Jalali (Persian) / Gregorian** date picker for **Jetpack Compose**, built as a **Kotlin Multiplatform** library (Android-first, coroutine-friendly).

## Features

- Calendar type set **in code only** (`CalendarType.JALALI` / `GREGORIAN`) — no in-UI switch
- **RTL for Jalali**, LTR for Gregorian (overridable)
- Bundled **Vazirmatn** font (Farsi-digits variant)
- **Style factory** — colors, typography, shapes, sizes, animations, strings, layout
- Light / dark via MaterialTheme or `DatePickerThemeMode` + `colorsForTheme`
- Sizes: **Small / Compact / Medium / Large**
- **Tablet**: `MonthCountMode.Auto` shows **two months** on wide screens
- First day of week set in code (`DayOfWeek.SATURDAY` / `SUNDAY` / …)
- Swipe months; tap month / year grids (decade navigation)
- Today button; clearer selection UX (hints, larger touch targets)
- Inline / dialog / bottom sheet; coroutine `StateFlow` / `SharedFlow`

## Install (JitPack)

```kotlin
dependencyResolutionManagement {
    repositories { maven(url = "https://jitpack.io") }
}

dependencies {
    implementation("com.github.ashrafimostafa:JalaliDatePicker:0.4.0")
}
```

## Quick start (configure calendar in code)

```kotlin
val state = rememberJalaliDatePickerState(
    calendarType = CalendarType.JALALI,       // or GREGORIAN — code only
    firstDayOfWeek = DayOfWeek.SATURDAY,      // customizable
    selectionMode = SelectionMode.SINGLE,     // or RANGE
    digitMode = DigitMode.PERSIAN,
)

JalaliDatePicker(
    state = state,
    size = DatePickerSize.Medium, // Small | Compact | Medium | Large
    styleFactory = JalaliDatePickerStyleFactory.builder()
        .monthCountMode(MonthCountMode.Auto) // 2 months on tablet
        .colors { scheme ->
            JalaliDatePickerDefaults.fromColorScheme(
                colorScheme = scheme,
                selectedDayContainerColor = scheme.primary,
            )
        }
        .build(),
)
```

### Theme / colors

```kotlin
// Follow app MaterialTheme (recommended)
JalaliDatePickerDefaults.colors(...)

// Or force light/dark palette
JalaliDatePickerDefaults.colorsForTheme(
    themeMode = DatePickerThemeMode.Dark,
    primary = Color(0xFF7DDBA3),
) { colors ->
    colors.copy(selectedDayContainerColor = Color(0xFF0B6E4F))
}
```

### Force one or two months

```kotlin
JalaliDatePickerStyleFactory.builder()
    .monthCountMode(MonthCountMode.Two) // or One / Auto
    .build()
```

## Font license

Vazirmatn is included under the SIL Open Font License — see `jalalidatepicker/src/androidMain/assets/OFL-Vazirmatn.txt`.

## License

Apache License 2.0 — see [LICENSE](LICENSE).
