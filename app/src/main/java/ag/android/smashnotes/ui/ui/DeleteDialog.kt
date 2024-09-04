package ag.android.smashnotes.ui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAlertBox(
    onDismissRequest: () -> Unit, confirmDeletion: () -> Unit, dialogText: String
) {
    BasicAlertDialog(onDismissRequest = { onDismissRequest() }) {

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Column {
                Text(
                    dialogText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {


                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = "Cancel")
                    }

                    TextButton(onClick = {
                        onDismissRequest()
                        confirmDeletion()
                    }) {
                        Text(text = "Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
            }

        }


    }
}
