package ir.mostafa.jalalidatepicker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.mostafa.jalalidatepicker.model.DatePickerSize
import ir.mostafa.jalalidatepicker.state.JalaliDatePickerState
import ir.mostafa.jalalidatepicker.ui.factory.JalaliDatePickerStyleFactory
import ir.mostafa.jalalidatepicker.ui.factory.rememberJalaliDatePickerStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JalaliDatePickerBottomSheet(
    state: JalaliDatePickerState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    size: DatePickerSize = DatePickerSize.Medium,
    styleFactory: JalaliDatePickerStyleFactory = JalaliDatePickerStyleFactory.default(),
    usePersianLabels: Boolean = true,
    onConfirm: () -> Unit = onDismissRequest,
    onCancel: () -> Unit = onDismissRequest,
) {
    val calendarType by state.calendarType.collectAsStateWithLifecycle()
    val style = rememberJalaliDatePickerStyle(
        factory = styleFactory,
        calendarType = calendarType,
        size = size,
        showActions = true,
        usePersianLabels = usePersianLabels,
    )
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = {
            state.cancel()
            onCancel()
        },
        sheetState = sheetState,
        containerColor = style.colors.containerColor,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 8.dp),
        ) {
            JalaliDatePicker(
                state = state,
                style = style,
                title = title,
                onConfirm = onConfirm,
                onCancel = onCancel,
            )
        }
    }
}
