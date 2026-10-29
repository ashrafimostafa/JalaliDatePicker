package ir.mostafa.jalalidatepicker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.state.JalaliDatePickerState

/**
 * Material 3 modal bottom sheet presentation of [JalaliDatePicker].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JalaliDatePickerBottomSheet(
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
    onConfirm: () -> Unit = onDismissRequest,
    onCancel: () -> Unit = onDismissRequest,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = {
            state.cancel()
            onCancel()
        },
        sheetState = sheetState,
        containerColor = colors.containerColor,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 8.dp),
        ) {
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
                onConfirm = onConfirm,
                onCancel = onCancel,
            )
        }
    }
}
