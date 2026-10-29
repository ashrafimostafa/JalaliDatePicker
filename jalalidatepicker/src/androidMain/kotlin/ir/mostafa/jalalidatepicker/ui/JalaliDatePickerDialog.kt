package ir.mostafa.jalalidatepicker.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.state.JalaliDatePickerState

/**
 * Material 3 dialog presentation of [JalaliDatePicker].
 */
@Composable
fun JalaliDatePickerDialog(
    state: JalaliDatePickerState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    showModeToggle: Boolean = true,
    confirmLabel: String = "تأیید",
    cancelLabel: String = "انصراف",
    usePersianLabels: Boolean = true,
    colors: JalaliDatePickerColors = JalaliDatePickerDefaults.colors(),
    sizes: JalaliDatePickerSizes = JalaliDatePickerDefaults.sizes(DatePickerSize.Medium),
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    onConfirm: () -> Unit = onDismissRequest,
    onCancel: () -> Unit = onDismissRequest,
) {
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
                title = title,
                showModeToggle = showModeToggle,
                showActions = true,
                confirmLabel = confirmLabel,
                cancelLabel = cancelLabel,
                usePersianLabels = usePersianLabels,
                colors = colors,
                sizes = sizes,
                onConfirm = {
                    onConfirm()
                },
                onCancel = {
                    onCancel()
                },
            )
        },
        containerColor = colors.containerColor,
    )
}
