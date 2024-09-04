package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.Screens
import ag.android.smashnotes.data.Fighter
import ag.android.smashnotes.data.noteCategory
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarView(
    currentRoute: String?,
    fighterSide: String?,
    selectedCharacter: Fighter?,
    selectedOpponent: Fighter?,
    navController: NavController,
    getCategoryName: (Int) -> Flow<noteCategory>
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val navigationIcon: @Composable (() -> Unit)? = remember(currentRoute) {
        if (currentRoute != null && !currentRoute.contains("categories") && !currentRoute.contains("links")) {
            {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = "Arrow Back"
                    )
                }
            }
        } else {
            null
        }
    }

    TopAppBar(title = {

        Text(
            // Set the TopAppBar title depending on what screen we're at
            when (currentRoute) {

                Screens.NewCategoryScreen.route + "/{categoryId}" -> {
                    val categoryId = currentBackStackEntry?.arguments?.getInt("categoryId")
                    if (categoryId == -1) {
                        "Add Category"
                    } else {
                        "Edit Category"
                    }
                }

                Screens.CharacterSelectionScreen.route + "/{fighterSide}" -> {
                    when (fighterSide) {
                        "left" -> "Choose Your Character"
                        else -> "Select Your Opponent"
                    }
                }

                "${Screens.NotesScreen.route}/{categoryId}" -> {
                    val categoryId = currentBackStackEntry?.arguments?.getString("categoryId")
                    val categoryName = getCategoryName(categoryId!!.toInt()).collectAsState(
                        initial = noteCategory(
                            name = "", myCharacter = "", myOpponent = "", position = 0
                        )
                    )
                    categoryName.value.name
                }

                Screens.NewNoteScreen.route + "/{noteId}" -> {
                    val noteId = currentBackStackEntry?.arguments?.getInt("noteId")
                    if (noteId == -1) {
                        "Add Note"
                    } else {
                        "Edit Note"
                    }
                }

                Screens.NewLinkScreen.route + "/{linkId}" -> {
                    val linkId = currentBackStackEntry?.arguments?.getInt("linkId")
                    if (linkId == -1) {
                        "Add Link"
                    } else {
                        "Edit Link"
                    }
                }

                else -> "SmashNotes"
            }
        )

    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ), navigationIcon = navigationIcon ?: {})


    MatchupCard(
        navController = navController,
        myCharacter = selectedCharacter,
        opponentCharacter = selectedOpponent
    )

    if (selectedCharacter != null && selectedOpponent != null) {
        var selectedTab by remember { mutableIntStateOf(0) }
        var selectedNotes = true

        TabRow(selectedTabIndex = selectedTab,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.onPrimary,
                        height = 2.dp
                    )
                }
            }

        ) {
            Tab(selected = selectedNotes, onClick = {
                selectedTab = 0
                selectedNotes = true
                navController.navigate(Screens.CategoriesScreen.route) { launchSingleTop = true }
            }) {
                Text(text = "Notes")
            }

            Tab(selected = !selectedNotes, onClick = {
                selectedTab = 1
                selectedNotes = false
                navController.navigate(Screens.LinksScreen.route) { launchSingleTop = true }
            }) {
                Text(text = "Links")
            }
        }
    }
}