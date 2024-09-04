package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.Screens
import ag.android.smashnotes.data.categories.CategoriesViewModel
import ag.android.smashnotes.data.noteCategory
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddNewCategoryScreen(
    paramCategory: Flow<noteCategory>,
    navController: NavController,
    addNewCategory: (noteCategory) -> Unit,
    updateCategory: (Int, String) -> Unit,
    categoriesViewModel: CategoriesViewModel
) {

    val currCategory by paramCategory.collectAsState(initial = noteCategory(-1, "", "", "", 0))

    var categoryName by remember { mutableStateOf("") }

    var displayedErrorMsg = remember {
        mutableStateOf("")
    }
    LaunchedEffect(currCategory) {
        categoryName = currCategory.name
    }

    LaunchedEffect(Unit) {
        categoriesViewModel.errorMessageOnInsert.collect { errorMessage ->
            if (errorMessage == null) {
                // if there's no error with insertion, navigate up
                navController.navigateUp()
            } else {
                // if there's an error, show the message
                displayedErrorMsg.value = errorMessage

            }
        }
    }

    val addOrUpdate = if (currCategory.id == -1) {
        "Add Category"
    } else {
        "Update Category"
    }


    Column {
        TextField(
            value = categoryName,
            onValueChange = { categoryName = it },
            label = { Text(text = "Category Name") },
            supportingText = {
                Text(
                    text = displayedErrorMsg.value,
                    color = MaterialTheme.colorScheme.error
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        )

        Row(Modifier.weight(1f)) {

            Button(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
                onClick = {
                    navController.popBackStack(
                        Screens.CategoriesScreen.route, inclusive = false
                    )
                }) {
                Text(text = "Cancel")
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
                onClick = {
                    if (currCategory.name == "") {
                        // this is a new category
                        addNewCategory(
                            noteCategory(
                                name = categoryName.trim(),
                                myCharacter = currCategory.myCharacter,
                                myOpponent = currCategory.myOpponent,
                                position = 1
                            )
                        )
                    } else { // update existing category
                        updateCategory(currCategory.id, categoryName.trim())
                    }
                }) {
                Text(text = addOrUpdate)
            }
        }
    }


}
