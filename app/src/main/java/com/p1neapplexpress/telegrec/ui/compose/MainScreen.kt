package com.p1neapplexpress.telegrec.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.p1neapplexpress.telegrec.R
import com.p1neapplexpress.telegrec.data.RecordingFormat
import com.p1neapplexpress.telegrec.ui.vm.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenView(
    title: String = stringResource(id = R.string.app_name),
    mainViewModel: MainViewModel = viewModel()
) {
    val enabledState by mainViewModel.enabledState.collectAsState()
    val recordingsFormatState by mainViewModel.recordingsFormatState.collectAsState()
    val savePathState by mainViewModel.savePathState.collectAsState()
    val fileNameMaskState by mainViewModel.fileNameMaskState.collectAsState()

    Column {
        TopAppBar(title = { Text(text = title) })
        Spacer(modifier = Modifier.height(16.dp))
        SwitchSetting(
            enabled = true,
            text = stringResource(R.string.enable),
            isChecked = enabledState,
            onCheckedChange = { mainViewModel.updateEnabledState() },
            onClick = { mainViewModel.updateEnabledState() })
        Spacer(modifier = Modifier.height(24.dp))
        SingleSelectionSetting(
            enabled = enabledState,
            title = stringResource(R.string.recording_format),
            selectedOption = recordingsFormatState,
            onOptionSelected = { mainViewModel.updateRecordingFormat(RecordingFormat.valueOf(it)) },
            options = RecordingFormat.entries.map { it.name }
        )
        Spacer(modifier = Modifier.height(8.dp))
        GetDirectoryTreeSetting(
            enabled = enabledState,
            title = stringResource(R.string.save_path),
            savedValue = savePathState,
            onValueChanged = { mainViewModel.updateSavePath(it) },
        )
        Spacer(modifier = Modifier.height(8.dp))
        EditTextSetting(
            enabled = enabledState,
            title = stringResource(R.string.file_name_mask),
            savedValue = fileNameMaskState,
            onValueChanged = { mainViewModel.updateFileNameMask(it) },
            dialogTitle = stringResource(R.string.date_date_when_recording_is_made_caller_id_caller_id),
        )
    }
}
