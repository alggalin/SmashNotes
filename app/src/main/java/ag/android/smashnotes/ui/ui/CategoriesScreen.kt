package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.Screens
import ag.android.smashnotes.data.categories.CategoriesViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp


@Composable
fun CategoryScreen(
    navigateTo: (String) -> Unit, categoriesViewModel: CategoriesViewModel
) {
    val categoriesState = categoriesViewModel.getMatchupCategories(
        categoriesViewModel.selectedCharacter.value?.characterId.orEmpty(),
        categoriesViewModel.opponentCharacter.value?.characterId.orEmpty()
    ).collectAsState(initial = listOf())


    if (categoriesViewModel.getSelectedCharacter() != null && categoriesViewModel.getOpponentCharacter() != null) {

        ReorderableListScreen(
            items = categoriesState.value.sortedBy { it.position },
            getItemKey = { it.id },
            updatePositions = { list -> categoriesViewModel.updateCategoryPositions(list) },
            navigateToNewItem = { navigateTo("${Screens.NewCategoryScreen.route}/-1") },
            buttonText = "Add Category",
            itemContent = { category, reorderableCollectionItemScope, interactionSource ->

                CardLayout(
                    cardContainerColor = MaterialTheme.colorScheme.tertiary,
                    cardContentColor = MaterialTheme.colorScheme.onTertiary,
                    textFontSize = 24.sp,
                    cardText = category.name,
                    deleteConfirmationText = "Are you sure you want to permanently delete this category" + " and all of its notes?",
                    reorderableCollectionItemScope = reorderableCollectionItemScope,
                    interactionSource = interactionSource,
                    onCardClick = {
                        categoriesViewModel.setCategory(category)
                        navigateTo("${Screens.NotesScreen.route}/${category.id}")
                    },
                    editCard = { navigateTo("${Screens.NewCategoryScreen.route}/${category.id}") },
                    deleteCard = { categoriesViewModel.deleteCategory(category) })
            },
            copyWithPosition = { item, newPosition -> item.copy(position = newPosition) }
        )

    }
}