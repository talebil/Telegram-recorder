package com.p1neapplexpress.telegrec.ui.compose.recordings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
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
import com.p1neapplexpress.telegrec.ui.vm.RecordingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordingsScreenView(
    title: String = stringResource(id = R.string.recordings),
    recordingsViewModel: RecordingsViewModel = viewModel()
) {

    val recordingsState by recordingsViewModel.recordingsState.collectAsState()
    val playbackState by recordingsViewModel.playbackState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(text = title) })
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            recordingsState.map {
                item {
                    RecordingListItem(recording = it) {
                        recordingsViewModel.playRecording(it)
                    }
                }
            }
        }
    }
}