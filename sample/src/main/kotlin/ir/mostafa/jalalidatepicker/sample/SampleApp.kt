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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.model.SelectionMode
import ir.mostafa.jalalidatepicker.ui.JalaliDatePicker
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerBottomSheet
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerDefaults
import ir.mostafa.jalalidatepicker.ui.JalaliDatePickerDialog
import ir.mostafa.jalalidatepicker.ui.sizes

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

    MaterialTheme(
        colorScheme = if (darkTheme) {
            darkColorScheme(primary = Color(0xFF7DDBA3))
        } else {
            lightColorScheme(primary = Color(0xFF0B6E4F))
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Jalali DatePicker") },
                    actions = {
                        TextButton(onClick = viewModel::toggleTheme) {
                            Text(if (darkTheme) "Light" else "Dark")
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
                Text("Selection mode", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectionMode == SelectionMode.SINGLE,
                        onClick = { viewModel.setSelectionMode(SelectionMode.SINGLE) },
                        label = { Text("Single") },
                    )
                    FilterChip(
                        selected = selectionMode == SelectionMode.RANGE,
                        onClick = { viewModel.setSelectionMode(SelectionMode.RANGE) },
                        label = { Text("Range") },
                    )
                }

                Text(
                    text = "Selected: ${selectedDate ?: "—"}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Range: ${selectedRange ?: "—"}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Event: $lastEvent",
                    style = MaterialTheme.typography.bodyMedium,
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = viewModel::openDialog) { Text("Open dialog") }
                    TextButton(onClick = viewModel::openSheet) { Text("Open sheet") }
                }

                Spacer(Modifier.height(8.dp))
                Text("Inline component", style = MaterialTheme.typography.titleSmall)

                JalaliDatePicker(
                    state = viewModel.pickerState,
                    modifier = Modifier.fillMaxWidth(),
                    showActions = true,
                    sizes = JalaliDatePickerDefaults.sizes(pickerSize),
                    colors = if (pickerSize == DatePickerSize.Large) {
                        JalaliDatePickerDefaults.colors(
                            selectedDayContainerColor = MaterialTheme.colorScheme.tertiary,
                            selectedDayContentColor = MaterialTheme.colorScheme.onTertiary,
                        )
                    } else {
                        JalaliDatePickerDefaults.colors()
                    },
                    onConfirm = { /* observed via SharedFlow in ViewModel */ },
                )
            }
        }

        if (showDialog) {
            JalaliDatePickerDialog(
                state = viewModel.pickerState,
                onDismissRequest = viewModel::closeDialog,
                sizes = JalaliDatePickerDefaults.sizes(pickerSize),
                onConfirm = viewModel::closeDialog,
                onCancel = viewModel::closeDialog,
            )
        }

        if (showSheet) {
            JalaliDatePickerBottomSheet(
                state = viewModel.pickerState,
                onDismissRequest = viewModel::closeSheet,
                sizes = JalaliDatePickerDefaults.sizes(pickerSize),
                onConfirm = viewModel::closeSheet,
                onCancel = viewModel::closeSheet,
            )
        }
    }
}
