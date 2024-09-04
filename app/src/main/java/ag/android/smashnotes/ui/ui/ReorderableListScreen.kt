package ag.android.smashnotes.ui.ui

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> ReorderableListScreen(
    items: List<T>,
    buttonText: String,
    getItemKey: (T) -> Int,
    updatePositions: (List<T>) -> Unit,
    navigateToNewItem: () -> Unit,
    itemContent: @Composable (T, ReorderableCollectionItemScope, MutableInteractionSource) -> Unit,
    copyWithPosition: (T, Int) -> T
) {
    val view = LocalView.current

    // Mutable state for the list that will be reorderable
    var list by remember {
        mutableStateOf(items)
    }

    LaunchedEffect(items) {
        list = items
    }

    val lazyListState = rememberLazyListState()

    // Reorderable state setup
    val reorderableLazyColumnState =
        rememberReorderableLazyListState(
            lazyListState = lazyListState
        ) { from, to ->
            list = list.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }


            // Update positions in the database
            val updatedList = list.mapIndexed { index, item ->
                copyWithPosition(item, index)
            }
            updatePositions(updatedList)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 8.dp),
            state = lazyListState
        ) {
            itemsIndexed(list, key = { _, item -> getItemKey(item)}) {index, item ->
                ReorderableItem(state = reorderableLazyColumnState, key = getItemKey(item)) {
                    val interactionSource = remember { MutableInteractionSource() }

                    itemContent(item, this, interactionSource)
                }
            }

        }

        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(8.dp),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            onClick = {
                navigateToNewItem()
            },
            content = {
                Icon(Icons.Filled.Add, "Floating action button")
                Text(text = buttonText)
            }
        )
    }
}