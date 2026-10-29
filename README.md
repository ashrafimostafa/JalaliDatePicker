# Jalali DatePicker

Highly customizable **Jalali (Persian) / Gregorian** date picker for **Jetpack Compose**, built as a **Kotlin Multiplatform** library (Android-first, coroutine-friendly).

## Features

- Jalali and Gregorian calendars with runtime toggle
- **RTL by default for Jalali**, LTR for Gregorian (overridable)
- Bundled **Vazirmatn** font (including Farsi-digits variant)
- **UI Style Factory** — customize colors, typography, shapes, sizes, animations, strings, layout
- Swipe between months; tap **month** / **year** for grids (years by decade)
- **Today** button; today is visually distinct (fill + border + dot)
- Fast month transitions (optimized animations)
- Single date and inclusive range selection
- Present as **inline**, **dialog**, or **modal bottom sheet**
- Material 3 light / dark theming
- Compact / Medium / **Large** size presets
- Customizable first day of week (Saturday for Jalali, Sunday for Gregorian)
- Coroutine-friendly API via `StateFlow` / `SharedFlow`

## Install (JitPack)

```kotlin
dependencyResolutionManagement {
    repositories { maven(url = "https://jitpack.io") }
}

dependencies {
    implementation("com.github.ashrafimostafa:JalaliDatePicker:0.2.0")
}
```

## Quick start

```kotlin
val state = rememberJalaliDatePickerState()

JalaliDatePicker(
    state = state,
    size = DatePickerSize.Large,
    styleFactory = JalaliDatePickerStyleFactory.default(), // Vazir + RTL for Jalali
)
```

## Customize everything (factory pattern)

```kotlin
val styleFactory = JalaliDatePickerStyleFactory.builder()
    .fontFamily(VazirFontFamily.FarsiDigits) // or VazirFontFamily.Default / your FontFamily
    .layoutDirection(LayoutDirection.Rtl)    // force RTL, or null for auto
    .containerCorner(24.dp)
    .animationsEnabled(true)
    .colors { scheme ->
        JalaliDatePickerColors(
            containerColor = scheme.surfaceContainerHigh,
            selectedDayContainerColor = scheme.tertiary,
            selectedDayContentColor = scheme.onTertiary,
            // ... every color token
            titleContentColor = scheme.onSurfaceVariant,
            headlineContentColor = scheme.onSurface,
            weekdayContentColor = scheme.onSurfaceVariant,
            dayContentColor = scheme.onSurface,
            disabledDayContentColor = scheme.onSurface.copy(alpha = 0.38f),
            todayContentColor = scheme.primary,
            todayContainerColor = Color.Transparent,
            rangeSelectionContainerColor = scheme.primaryContainer.copy(alpha = 0.55f),
            navigationContentColor = scheme.onSurfaceVariant,
            dividerColor = scheme.outlineVariant,
            actionContentColor = scheme.primary,
            modeToggleContentColor = scheme.primary,
        )
    }
    .strings { persian ->
        JalaliDatePickerStringsFactory.create(persian).copy(confirm = "ثبت")
    }
    .animations {
        JalaliDatePickerAnimationsFactory.create(monthChangeMillis = 400)
    }
    .build()

JalaliDatePicker(
    state = state,
    styleFactory = styleFactory,
    showActions = true,
)
```

You can also implement `JalaliDatePickerStyleFactory` yourself for full control over each product (`createColors`, `createTypography`, `createShapes`, …).

## Coroutine / ViewModel usage

```kotlin
class MyViewModel : ViewModel() {
    val pickerState = JalaliDatePickerState()

    init {
        viewModelScope.launch {
            pickerState.selectionEvents.collect { /* ... */ }
        }
    }
}
```

## Modules

| Module | Role |
|--------|------|
| `jalalidatepicker` | KMP library (`commonMain` calendar + state, `androidMain` Compose UI) |
| `sample` | Demo app (ViewModel + factory customization) |

## Font license

Vazirmatn is included under the SIL Open Font License — see `jalalidatepicker/src/androidMain/assets/OFL-Vazirmatn.txt`.

## License

Apache License 2.0 — see [LICENSE](LICENSE).
