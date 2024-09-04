package ag.android.smashnotes

sealed class Screens(val route: String) {
    object CategoriesScreen: Screens("categories")
    object NotesScreen: Screens("notes")
    object LinksScreen: Screens("links")
    object CharacterSelectionScreen: Screens("characterselection")
    object NewCategoryScreen: Screens("newcategory")
    object NewNoteScreen: Screens("newnote")
    object NewLinkScreen: Screens("newlink")

}