package ir.mostafa.jalalidatepicker.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.state.JalaliDatePickerState
import ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerStyleFactory
import ir.mostafa.jalalidatepicker.ui.factory.rememberJalaliDatePickerStyle

/**
 * Material 3 dialog presentation of [JalaliDatePicker].
 */
@Composable
fun JalaliDatePickerDialog(
    state: JalaliDatePickerState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    size: DatePickerSize = DatePickerSize.Medium,
    styleFactory: JalaliDatePickerStyleFactory = JalaliDatePickerStyleFactory.default(),
    showModeToggle: Boolean = true,
    usePersianLabels: Boolean = true,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    onConfirm: () -> Unit = onDismissRequest,
    onCancel: () -> Unit = onDismissRequest,
) {
    val calendarType by state.calendarType.collectAsStateWithLifecycle()
    val style = rememberJalaliDatePickerStyle(
        factory = styleFactory,
        calendarType = calendarType,
        size = size,
        showModeToggle = showModeToggle,
        showActions = true,
        usePersianLabels = usePersianLabels,
    )
    AlertDialog(
        onDismissRequest = {
            state.cancel()
            onCancel()
        },
        confirmButton = {},
        modifier = modifier,
        properties = properties,
        text = {
            JalaliDatePicker(
                state = state,
                style = style,
                title = title,
                onConfirm = onConfirm,
                onCancel = onCancel,
            )
        },
        containerColor = style.colors.containerColor,
    )
}
