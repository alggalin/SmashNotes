package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.R
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableCollectionItemScope

@Composable
fun CardLayout(
    cardContainerColor: Color,
    cardContentColor: Color,
    textFontSize: TextUnit,
    cardText: String,
    deleteConfirmationText: String,
    reorderableCollectionItemScope: ReorderableCollectionItemScope,
    interactionSource: MutableInteractionSource,
    onCardClick: () -> Unit,
    editCard: () -> Unit,
    deleteCard: () -> Unit
) {
    val view = LocalView.current
    var openDropDownMenu by remember { mutableStateOf(false) }
    var showDeleteOption by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .clickable {
                onCardClick()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardContainerColor,
            contentColor = cardContentColor
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = with(reorderableCollectionItemScope) {
                    Modifier
                        .draggableHandle(onDragStarted = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                view.performHapticFeedback(HapticFeedbackConstants.DRAG_START)
                            }
                        }, onDragStopped = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                            }
                        }, interactionSource = interactionSource
                        )
                },
                painter = painterResource(id = R.drawable.baseline_drag_handle_24),
                contentDescription = "Draggable Icon"

            )

            Text(
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 4.dp),
                fontSize = textFontSize,
                text = cardText
            )

            Box(
                modifier = Modifier.weight(0.3f)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            openDropDownMenu = true
                        },
                    painter = painterResource(id = R.drawable.baseline_expand_menu),
                    contentDescription = "Expand Menu"
                )

                DropdownMenu(
                    expanded = openDropDownMenu,
                    onDismissRequest = { openDropDownMenu = false },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    DropdownMenuItem(text = { Text(text = "Edit") }, onClick = {
                        openDropDownMenu = false
                        editCard()
                    })
                    DropdownMenuItem(text = { Text(text = "Delete") }, onClick = {
                        openDropDownMenu = false
                        showDeleteOption = true
                    })
                }

                if (showDeleteOption) {
                    DeleteAlertBox(
                        onDismissRequest = { showDeleteOption = false },
                        confirmDeletion = { deleteCard() },
                        dialogText = deleteConfirmationText
                    )
                }
            }
        }
    }
}