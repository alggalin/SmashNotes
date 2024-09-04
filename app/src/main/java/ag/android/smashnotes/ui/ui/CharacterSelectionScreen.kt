package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.R
import ag.android.smashnotes.Screens
import ag.android.smashnotes.data.Fighter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage


@Composable
fun CharacterSelectionScreen(
    setCharacter: (Fighter) -> Unit,
    setOpponent: (Fighter) -> Unit,
    navController: NavController,
    fighterSide: String
) {
    // Use remember to avoid recomputing the list on every recomposition
    val characters = remember {
        getCharacters()
    }

    LazyVerticalGrid(
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
        columns = GridCells.Fixed(5)
    ) {
        items(characters.size) { index ->
            val character = characters[index]

            CharacterItem(character = character, onClick = {
                if (fighterSide == "left") {
                    setCharacter(character)
                } else if (fighterSide == "right") {
                    setOpponent(character)
                }
                navController.popBackStack(
                    Screens.CategoriesScreen.route, inclusive = false
                )
            })

        }
    }

}

@Composable
fun CharacterItem(character: Fighter, onClick: () -> Unit) {
    AsyncImage(model = character.icon,
        contentDescription = "${character.name} Icon",
        alignment = Alignment.Center,
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() }
    )

}

// List containing all of the characters
fun getCharacters() = listOf(
    Fighter("Mario", "mario", R.drawable.mario_0),
    Fighter("Donkey Kong", "donkey_kong", R.drawable.donkey_kong_0),
    Fighter("Link", "link", R.drawable.link_0),
    Fighter("Samus", "samus", R.drawable.samus_0),
    Fighter("Dark Samus", "dark_samu", R.drawable.dark_samus_0),
    Fighter("Yoshi", "yoshi", R.drawable.yoshi_0),
    Fighter("Kirby", "kirby", R.drawable.kirby_0),
    Fighter("Fox", "fox", R.drawable.fox_0),
    Fighter("Pikachu", "pikachu", R.drawable.pikachu_0),
    Fighter("Luigi", "luigi", R.drawable.luigi_0),
    Fighter("Ness", "ness", R.drawable.ness_0),
    Fighter("Captain Falcon", "captain_falcon", R.drawable.captain_falcon_0),
    Fighter("Jigglypuff", "jigglypuff", R.drawable.jigglypuff_0),
    Fighter("Peach", "peach", R.drawable.peach_0),
    Fighter("Daisy", "daisy", R.drawable.daisy_0),
    Fighter("Bowser", "bowser", R.drawable.bowser_0),
    Fighter("Ice Climbers", "ice_climbers", R.drawable.ice_climbers_0),
    Fighter("Sheik", "sheik", R.drawable.sheik_0),
    Fighter("Zelda", "zelda", R.drawable.zelda_0),
    Fighter("Dr. Mario", "dr_mario", R.drawable.dr__mario_0),
    Fighter("Pichu", "pichu", R.drawable.pichu_0),
    Fighter("Falco", "falco", R.drawable.falco_0),
    Fighter("Marth", "marth", R.drawable.marth_0),
    Fighter("Lucina", "lucina", R.drawable.lucina_0),
    Fighter("Young Link", "young_link", R.drawable.young_link_0),
    Fighter("Ganondorf", "ganondorf", R.drawable.ganondorf_0),
    Fighter("Mewtwo", "mewtwo", R.drawable.mewtwo_0),
    Fighter("Roy", "roy", R.drawable.roy_0),
    Fighter("Chrom", "chrom", R.drawable.chrom_0),
    Fighter("Mr. Game & Watch", "game_and_watch", R.drawable.mr__game___watch_0),
    Fighter("Meta Knight", "meta_knight", R.drawable.meta_knight_0),
    Fighter("Pit", "pit", R.drawable.pit_0),
    Fighter("Dark Pit", "dark_pit", R.drawable.dark_pit_0),
    Fighter("Zero Suit Samus", "zss", R.drawable.zero_suit_samus_0),
    Fighter("Wario", "wario", R.drawable.wario_0),
    Fighter("Snake", "snake", R.drawable.snake_0),
    Fighter("Ike", "ike", R.drawable.ike_0),
    Fighter("Pokemon Trainer", "poke_trainer", R.drawable.pok_mon_trainer_0),
    Fighter("Squirtle", "squirtle", R.drawable.squirtle_0),
    Fighter("Ivysaur", "ivysaur", R.drawable.ivysaur_0),
    Fighter("Charizard", "charizard", R.drawable.charizard_0),
    Fighter("Diddy Kong", "diddy_kong", R.drawable.diddy_kong_0),
    Fighter("Lucas", "lucas", R.drawable.lucas_0),
    Fighter("Sonic", "sonic", R.drawable.sonic_0),
    Fighter("King Dedede", "dedede", R.drawable.king_dedede_0),
    Fighter("Olimar", "olimar", R.drawable.pikmin_0),
    Fighter("Lucario", "lucario", R.drawable.lucario_0),
    Fighter("R.O.B.", "rob", R.drawable.r_o_b__0),
    Fighter("Toon Link", "tlink", R.drawable.toon_link_0),
    Fighter("Wolf", "wolf", R.drawable.wolf_0),
    Fighter("Villager", "villager", R.drawable.villager_0),
    Fighter("Mega Man", "mega_man", R.drawable.mega_man_0),
    Fighter("Wii Fit Trainer", "wii_fit", R.drawable.wii_fit_trainer_0),
    Fighter("Rosalina & Luma", "rosalina", R.drawable.rosalina___luma_0),
    Fighter("Little Mac", "little mac", R.drawable.little_mac_0),
    Fighter("Greninja", "greninja", R.drawable.greninja_0),
    Fighter("Palutena", "palu", R.drawable.palutena_0),
    Fighter("Pac-Man", "pac_man", R.drawable.pac_man_0),
    Fighter("Robin", "robin", R.drawable.robin_0),
    Fighter("Shulk", "shulk", R.drawable.shulk_0),
    Fighter("Bowser Jr.", "bowser_jr", R.drawable.koopalings__bowser_jr__),
    Fighter("Duck Hunt", "duck_hunt", R.drawable.duck_hunt_0),
    Fighter("Ryu", "ryu", R.drawable.ryu_0),
    Fighter("Ken", "ken", R.drawable.ken_0),
    Fighter("Cloud", "cloud", R.drawable.cloud_0),
    Fighter("Corrin", "corrin", R.drawable.corrin_0),
    Fighter("Bayonetta", "bayo", R.drawable.bayonetta_0),
    Fighter("Inkling", "inkling", R.drawable.inkling_0),
    Fighter("Ridley", "ridley", R.drawable.ridley_0),
    Fighter("Simon", "simon", R.drawable.simon_0),
    Fighter("Richter", "richter", R.drawable.richter_0),
    Fighter("King K. Rool", "k_rool", R.drawable.king_k__rool_0),
    Fighter("Isabelle", "isabelle", R.drawable.isabelle_0),
    Fighter("Incineroar", "incineroar", R.drawable.incineroar_0),
    Fighter("Piranha Plant", "plant", R.drawable.piranha_plant_0),
    Fighter("Joker", "joker", R.drawable.joker_0),
    Fighter("Hero", "hero", R.drawable.hero_0),
    Fighter("Banjo & Kazooie", "banjo", R.drawable.banjo___kazooie_0),
    Fighter("Terry", "terry", R.drawable.terry_0),
    Fighter("Byleth", "byleth", R.drawable.byleth_0),
    Fighter("Min Min", "min_min", R.drawable.min_min_0),
    Fighter("Steve", "steve", R.drawable.minecraft_0),
    Fighter("Sephiroth", "sephiroth", R.drawable.sephiroth_0),
    Fighter("Pyra", "pyra", R.drawable.pyra_0),
    Fighter("Mythra", "mythra", R.drawable.mythra_0),
    Fighter("Kazuya", "kazuya", R.drawable.kazuya_0),
    Fighter("Sora", "sora", R.drawable.sora_0),
    Fighter("Mii Brawler", "mii_brawler", R.drawable.mii_brawler),
    Fighter("Mii Swordfighter", "mii_sword_fighter", R.drawable.mii_swordfighter),
    Fighter("Mii Gunner", "mii_gunner", R.drawable.mii_gunner),
    Fighter("", "", R.drawable.random_icon)
)