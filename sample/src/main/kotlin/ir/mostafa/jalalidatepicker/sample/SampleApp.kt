package ir.mostafa.jalalidatepicker.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.model.SelectionMode
import ir.mostafa.jalalidatepicker.ui.JalaliDatePicker
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerBottomSheet
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerColors
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerDialog
import ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerStyleFactory
import ir.mostafa.jalalidatepicker.ui.font.VazirFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleApp(viewModel: SampleViewModel = viewModel()) {
    val darkTheme by viewModel.darkTheme.collectAsStateWithLifecycle()
    val pickerSize by viewModel.pickerSize.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedRange by viewModel.selectedRange.collectAsStateWithLifecycle()
    val lastEvent by viewModel.lastEvent.collectAsStateWithLifecycle()
    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()
    val showSheet by viewModel.showSheet.collectAsStateWithLifecycle()
    val selectionMode by viewModel.pickerState.selectionMode.collectAsStateWithLifecycle()

    val styleFactory = remember(pickerSize) {
        JalaliDatePickerStyleFactory.builder()
            .fontFamily(VazirFontFamily.FarsiDigits)
            .animationsEnabled(true)
            .containerCorner(24.dp)
            .colors { scheme ->
                JalaliDatePickerColors(
                    containerColor = scheme.surfaceContainerHigh,
                    titleContentColor = scheme.onSurfaceVariant,
                    headlineContentColor = scheme.onSurface,
                    weekdayContentColor = scheme.onSurfaceVariant,
                    dayContentColor = scheme.onSurface,
                    disabledDayContentColor = scheme.onSurface.copy(alpha = 0.38f),
                    selectedDayContainerColor = if (pickerSize == DatePickerSize.Large) {
                        scheme.tertiary
                    } else {
                        scheme.primary
                    },
                    selectedDayContentColor = if (pickerSize == DatePickerSize.Large) {
                        scheme.onTertiary
                    } else {
                        scheme.onPrimary
                    },
                    todayContentColor = scheme.primary,
                    todayContainerColor = scheme.primaryContainer.copy(alpha = 0.55f),
                    rangeSelectionContainerColor = scheme.primaryContainer.copy(alpha = 0.55f),
                    navigationContentColor = scheme.onSurfaceVariant,
                    dividerColor = scheme.outlineVariant,
                    actionContentColor = scheme.primary,
                    modeToggleContentColor = scheme.primary,
                )
            }
            .build()
    }

    MaterialTheme(
        colorScheme = if (darkTheme) {
            darkColorScheme(primary = Color(0xFF7DDBA3))
        } else {
            lightColorScheme(primary = Color(0xFF0B6E4F))
        },
        typography = MaterialTheme.typography.let { base ->
            // App chrome can stay default; picker uses Vazir via style factory.
            base
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("انتخابگر تاریخ جلالی") },
                    actions = {
                        TextButton(onClick = viewModel::toggleTheme) {
                            Text(if (darkTheme) "روشن" else "تاریک")
                        }
                        TextButton(onClick = viewModel::cycleSize) {
                            Text(pickerSize.name)
                        }
                    },
                )
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("حالت انتخاب", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectionMode == SelectionMode.SINGLE,
                        onClick = { viewModel.setSelectionMode(SelectionMode.SINGLE) },
                        label = { Text("تک‌تاریخ") },
                    )
                    FilterChip(
                        selected = selectionMode == SelectionMode.RANGE,
                        onClick = { viewModel.setSelectionMode(SelectionMode.RANGE) },
                        label = { Text("بازه") },
                    )
                }

                Text("انتخاب‌شده: ${selectedDate ?: "—"}")
                Text("بازه: ${selectedRange ?: "—"}")
                Text("رویداد: $lastEvent")

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = viewModel::openDialog) { Text("دیالوگ") }
                    TextButton(onClick = viewModel::openSheet) { Text("شیت") }
                }

                Spacer(Modifier.height(8.dp))
                Text("کامپوننت درون‌صفحه", style = MaterialTheme.typography.titleSmall)

                JalaliDatePicker(
                    state = viewModel.pickerState,
                    modifier = Modifier.fillMaxWidth(),
                    size = pickerSize,
                    styleFactory = styleFactory,
                    showActions = true,
                )
            }
        }

        if (showDialog) {
            JalaliDatePickerDialog(
                state = viewModel.pickerState,
                onDismissRequest = viewModel::closeDialog,
                size = pickerSize,
                styleFactory = styleFactory,
                onConfirm = viewModel::closeDialog,
                onCancel = viewModel::closeDialog,
            )
        }

        if (showSheet) {
            JalaliDatePickerBottomSheet(
                state = viewModel.pickerState,
                onDismissRequest = viewModel::closeSheet,
                size = pickerSize,
                styleFactory = styleFactory,
                onConfirm = viewModel::closeSheet,
                onCancel = viewModel::closeSheet,
            )
        }
    }
}
