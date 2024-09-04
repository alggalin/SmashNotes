package ag.android.smashnotes


import ag.android.smashnotes.data.Graph
import ag.android.smashnotes.data.categories.CategoriesViewModel
import ag.android.smashnotes.data.categories.CategoriesViewModelFactory
import ag.android.smashnotes.data.categories.DataStoreRepository
import ag.android.smashnotes.data.links.LinksViewModel
import ag.android.smashnotes.data.matchups.NotesViewModel
import ag.android.smashnotes.data.noteCategory
import ag.android.smashnotes.ui.theme.SmashNotesTheme
import ag.android.smashnotes.ui.theme.themeBackground
import ag.android.smashnotes.ui.ui.AddNewCategoryScreen
import ag.android.smashnotes.ui.ui.AddNewLinkScreen
import ag.android.smashnotes.ui.ui.AddNewNoteScreen
import ag.android.smashnotes.ui.ui.AppBarView
import ag.android.smashnotes.ui.ui.CategoryScreen
import ag.android.smashnotes.ui.ui.CharacterSelectionScreen
import ag.android.smashnotes.ui.ui.LinksScreen
import ag.android.smashnotes.ui.ui.NoteViewState
import ag.android.smashnotes.ui.ui.NotesScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create instances of repository and viewmodel factory
        val dataStoreRepository = DataStoreRepository(applicationContext)
        val categoryRepository = Graph.categoryRepository
        val categoryViewModelFactory =
            CategoriesViewModelFactory(dataStoreRepository, categoryRepository)

        setContent {

            /**
             * Assigning ViewModel & navController to be used throughout the app
             */
            val categoryViewModel: CategoriesViewModel by viewModels() { categoryViewModelFactory }
            val notesViewModel: NotesViewModel by viewModels()
            val linksViewModel: LinksViewModel by viewModels()
            val navController = rememberNavController()

            SmashNotesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SmashNotesApp(
                        navController,
                        categoryViewModel,
                        notesViewModel,
                        linksViewModel,
                        innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun SmashNotesApp(
    navController: NavHostController,
    categoryViewModel: CategoriesViewModel,
    notesViewModel: NotesViewModel,
    linksViewModel: LinksViewModel,
    innerPadding: PaddingValues
) {

    val selectedCharacter by categoryViewModel.selectedCharacter
    val selectedOpponent by categoryViewModel.opponentCharacter
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val fighterSide = currentBackStackEntry?.arguments?.getString("fighterSide")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        containerColor = themeBackground,
        topBar = {
            Column(Modifier.fillMaxWidth()) {
                AppBarView(
                    currentRoute = currentRoute,
                    fighterSide = fighterSide,
                    selectedCharacter = selectedCharacter,
                    selectedOpponent = selectedOpponent,
                    navController = navController,
                    getCategoryName = { catId: Int -> categoryViewModel.getCategoryById(catId) }
                )

            }

        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screens.CategoriesScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Screens.CategoriesScreen.route) {
                    CategoryScreen(
                        navigateTo = { route ->
                            navController.navigate(route) {
                                launchSingleTop = true
                            }
                        },
                        categoriesViewModel = categoryViewModel
                    )
                }

                // Define the "characterSelection" destination with a required argument of "selectedFighter"
                composable(route = "${Screens.CharacterSelectionScreen.route}/{fighterSide}") { backStackEntry ->
                    // Retrieve "selectedFighter" argument from the backStackEntry
                    val fighterSide = backStackEntry.arguments?.getString("fighterSide") ?: "left"
                    CharacterSelectionScreen(
                        setCharacter = { character -> categoryViewModel.setCharacter(character) },
                        setOpponent = { character -> categoryViewModel.setOpponent(character) },
                        navController = navController,
                        fighterSide
                    )
                }

                val saveCat: (noteCategory) -> Unit =
                    { name -> categoryViewModel.addCategory(name) }
                val updateCat: (Int, String) -> Unit = { id, newName ->
                    categoryViewModel.updateCategory(
                        id,
                        newName
                    )
                }
                // Navigating to screen to add new category for matchup
                composable(route = Screens.NewCategoryScreen.route + "/{categoryId}",
                    arguments = listOf(
                        navArgument("categoryId") {
                            type = NavType.IntType
                            defaultValue = -1
                            nullable = false
                        }
                    )) { categoryName ->
                    val catId: Int = categoryName.arguments?.getInt("categoryId") ?: -1
                    val currCategory = categoryViewModel.getCategoryById(catId)
                    AddNewCategoryScreen(
                        paramCategory = currCategory,
                        navController = navController,
                        addNewCategory = saveCat,
                        updateCategory = updateCat,
                        categoriesViewModel = categoryViewModel
                    )
                }

                // Define "NotesScreen" destination with required categoryId parameter
                composable(route = "${Screens.NotesScreen.route}/{categoryId}") { backStackEntry ->
                    // Extracting the id from the route
                    val categoryId = backStackEntry.arguments?.getString("categoryId")
                    categoryId?.let {
                        NotesScreen(
                            navigateTo = { navController.navigate(it) { launchSingleTop = true } },
                            categoryId = categoryId.toInt(),
                            deleteNote = { note -> notesViewModel.deleteNote(note) },
                            getMatchupNotes = { charId, oppId, catId ->
                                notesViewModel.getMatchupNotes(
                                    charId,
                                    oppId,
                                    catId
                                )
                            },
                            selectedCharacter = categoryViewModel.getSelectedCharacter()!!,
                            selectedOpponent = categoryViewModel.getOpponentCharacter()!!,
                            updateNotesPositions = { notesList ->
                                notesViewModel.updateNotesPositions(
                                    notesList
                                )
                            }
                        )
                    }

                }


                composable(route = Screens.NewNoteScreen.route + "/{noteId}",
                    arguments = listOf(
                        navArgument("noteId") {
                            type = NavType.IntType
                            defaultValue = -1
                            nullable = false
                        }
                    )) { entry ->
                    val noteId =
                        if (entry.arguments != null) entry.arguments!!.getInt("noteId") else -1

                    val viewState = NoteViewState(
                        noteDescription = notesViewModel.noteDescriptionState,
                        selectedCharacter = categoryViewModel.getSelectedCharacter(),
                        selectedOpponent = categoryViewModel.getOpponentCharacter(),
                        selectedCategory = categoryViewModel.getSelectedCategory(),
                        errorMessageOnInsert = notesViewModel.errorMessageOnInsert
                    )
                    AddNewNoteScreen(
                        viewState = viewState,
                        noteId = noteId,
                        navigateBack = { navController.navigateUp() },
                        onNoteDescriptionChange = { notesViewModel.updateNoteDescriptionState(it) },
                        addNote = { note -> notesViewModel.addNote(note) },
                        updateNote = { note -> notesViewModel.updateNote(note) },
                        loadNoteDescription = { id -> notesViewModel.loadNoteDescription(id) }
                    )
                }

                composable(route = Screens.LinksScreen.route) {
                    LinksScreen(
                        selectedCharacter = categoryViewModel.getSelectedCharacter()!!,
                        selectedOpponent = categoryViewModel.getOpponentCharacter()!!,
                        navigateTo = { link -> navController.navigate(link) },
                        deleteLink = { link -> linksViewModel.deleteLink(link) },
                        getMatchupLinks = { myChar, myOpponent ->
                            linksViewModel.getMatchupLinks(
                                myChar,
                                myOpponent
                            )
                        },
                        updateLinksPositions = { linksList ->
                            linksViewModel.updateLinksPositions(linksList)
                        }
                    )
                }

                composable(route = Screens.NewLinkScreen.route + "/{linkId}",
                    arguments = listOf(
                        navArgument("linkId") {
                            type = NavType.IntType
                            defaultValue = -1
                            nullable = false
                        }
                    )) { linkName ->
                    val linkId: Int = linkName.arguments?.getInt("linkId") ?: -1

                    val currLink = linksViewModel.getLinkById(linkId)
                    AddNewLinkScreen(
                        linkId = linkId,
                        linkDescriptionState = linksViewModel.linkDescriptionState,
                        linkTextState = linksViewModel.linkTextState,
                        errorMessageOnInsert = linksViewModel.errorMessageOnInsert,
                        myOpponent = categoryViewModel.getOpponentCharacter()!!,
                        myCharacter = categoryViewModel.getSelectedCharacter()!!,
                        navigateBack = { navController.navigateUp() },
                        addLink = { link -> linksViewModel.addLink(link) },
                        updateLink = { link -> linksViewModel.updateLink(link) },
                        loadLinkDescriptor = { id -> linksViewModel.loadLinkInfo(id) },
                        updateLinkDescriptionState = { text ->
                            linksViewModel.updateLinkDescriptionState(
                                text
                            )
                        },
                        updateLinkTextState = { text -> linksViewModel.updateLinkTextState(text) }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmashNotesTheme {

    }
}