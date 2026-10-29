# Jalali DatePicker

Highly customizable **Jalali (Persian) / Gregorian** date picker for **Jetpack Compose**, built as a **Kotlin Multiplatform** library (Android-first, coroutine-friendly).

## Features

- Jalali and Gregorian calendars with runtime toggle
- Single date and inclusive range selection
- Present as **inline**, **dialog**, or **modal bottom sheet**
- Material 3 light / dark theming with full color overrides
- Compact / Medium / **Large** size presets
- Customizable first day of week (defaults: Saturday for Jalali, Sunday for Gregorian)
- Persian or Latin digits and labels
- Coroutine-friendly API via `StateFlow` / `SharedFlow` (no `CoroutineScope` required to construct state)

## Install (JitPack)

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        maven(url = "https://jitpack.io")
    }
}
```

```kotlin
// module build.gradle.kts
dependencies {
    implementation("com.github.USERNAME:JalaliDatePicker:Tag")
}
```

Replace `USERNAME` with your GitHub username and `Tag` with a release tag (for example `0.1.0`).

## Quick start

```kotlin
val state = rememberJalaliDatePickerState(
    selectionMode = SelectionMode.SINGLE, // or RANGE
)

JalaliDatePicker(
    state = state,
    sizes = JalaliDatePickerDefaults.sizes(DatePickerSize.Large),
    colors = JalaliDatePickerDefaults.colors(
        selectedDayContainerColor = MaterialTheme.colorScheme.tertiary,
    ),
)

// Dialog / sheet
JalaliDatePickerDialog(state = state, onDismissRequest = { /* ... */ })
JalaliDatePickerBottomSheet(state = state, onDismissRequest = { /* ... */ })
```

## Coroutine / ViewModel usage

```kotlin
class MyViewModel : ViewModel() {
    val pickerState = JalaliDatePickerState()

    val selectedDate = pickerState.selectedDate
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    init {
        viewModelScope.launch {
            pickerState.selectionEvents.collect { event ->
                when (event) {
                    is DatePickerEvent.DateConfirmed -> { /* persist */ }
                    is DatePickerEvent.RangeConfirmed -> { /* persist range */ }
                    DatePickerEvent.Cancelled -> { /* ... */ }
                    is DatePickerEvent.RangeStartSelected -> { /* ... */ }
                }
            }
        }
    }
}
```

## Modules

| Module | Role |
|--------|------|
| `jalalidatepicker` | KMP library (`commonMain` calendar + state, `androidMain` Compose UI) |
| `sample` | Demo app (ViewModel + inline / dialog / sheet) |

## Sample app

```bash
./gradlew :sample:assembleDebug
```

## License

Apache License 2.0 — see [LICENSE](LICENSE).
