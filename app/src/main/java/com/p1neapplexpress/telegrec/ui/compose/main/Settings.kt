package com.p1neapplexpress.telegrec.ui.compose.main

import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.p1neapplexpress.telegrec.ui.theme.DarkGray2
import com.p1neapplexpress.telegrec.util.UriHelper.getPath


@Composable
fun SwitchSetting(
    enabled: Boolean,
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    val inDarkTheme = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .height(72.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { if (enabled) onClick.invoke() }
            .alpha(if (enabled) 1f else 0.7f),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(if (inDarkTheme) DarkGray2 else Color.White)
                .fillMaxSize()
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

@Composable
fun SingleSelectionSetting(
    enabled: Boolean,
    title: String,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    options: List<String>,
) {
    val inDarkTheme = isSystemInDarkTheme()
    val dialogVisible = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (inDarkTheme) DarkGray2 else Color.White)
                .clickable { if (enabled) dialogVisible.value = true }
                .alpha(if (enabled) 1f else 0.7f)
                .padding(16.dp),
        ) {
            Text(text = title, fontSize = 16.sp)
            Text(modifier = Modifier.alpha(0.8f), text = selectedOption, fontSize = 14.sp)
        }
    }

    if (dialogVisible.value) {
        Dialog(onDismissRequest = { dialogVisible.value = false }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    options.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOptionSelected(option); dialogVisible.value = false }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option,
                                color = if (inDarkTheme) Color.White else Color.Black,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditTextSetting(
    enabled: Boolean,
    title: String,
    savedValue: String,
    onValueChanged: (String) -> Unit,
    dialogTitle: String
) {
    val inDarkTheme = isSystemInDarkTheme()
    val dialogVisible = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (inDarkTheme) DarkGray2 else Color.White)
                .clickable { if (enabled) dialogVisible.value = true }
                .alpha(if (enabled) 1f else 0.7f)
                .padding(16.dp),
        ) {
            Text(text = title, fontSize = 16.sp)
            Text(modifier = Modifier.alpha(0.8f), text = savedValue, fontSize = 14.sp)
        }
    }

    if (dialogVisible.value) {
        Dialog(onDismissRequest = { dialogVisible.value = false }) {
            var text by remember { mutableStateOf(savedValue) }

            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dialogTitle,
                        color = if (inDarkTheme) Color.White else Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                            onValueChanged.invoke(it)
                        },
                        colors = TextFieldDefaults.colors().copy(
                            disabledTextColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun GetDirectoryTreeSetting(
    enabled: Boolean,
    title: String,
    savedValue: String,
    onValueChanged: (String) -> Unit
) {
    val context = LocalContext.current
    val inDarkTheme = isSystemInDarkTheme()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        val docUri = DocumentsContract.buildDocumentUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )
        val path: String = getPath(context, docUri)

        onValueChanged.invoke(path)
    }

    Card(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (inDarkTheme) DarkGray2 else Color.White)
                .clickable { if (enabled) launcher.launch(Uri.parse(savedValue)) }
                .alpha(if (enabled) 1f else 0.7f)
                .padding(16.dp),
        ) {
            Text(text = title, fontSize = 16.sp)
            Text(modifier = Modifier.alpha(0.8f), text = savedValue, fontSize = 14.sp)
        }
    }
}
