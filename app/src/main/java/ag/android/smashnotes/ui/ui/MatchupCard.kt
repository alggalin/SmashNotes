package ag.android.smashnotes.ui.ui

import ag.android.smashnotes.R
import ag.android.smashnotes.Screens
import ag.android.smashnotes.data.Fighter
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController

@Composable
fun MatchupCard(
    navController: NavController,
    myCharacter: Fighter?,
    opponentCharacter: Fighter?
) {


    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .size(220.dp)
            .zIndex(1f),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {

        Row(
            modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(myCharacter?.icon ?: R.drawable.stage_2_random),
                contentDescription = "${myCharacter?.name ?: "Choose your Character"} Icon",
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .clickable {
                        navController.navigate("${Screens.CharacterSelectionScreen.route}/left") {
                            launchSingleTop = true
                        }
                    }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                "VS",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Image(
                painter = painterResource(opponentCharacter?.icon ?: R.drawable.stage_2_random),
                contentDescription = "${opponentCharacter?.name ?: "Select your opponent"} Icon",
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .clickable {
                        navController.navigate("${Screens.CharacterSelectionScreen.route}/right") {
                            launchSingleTop = true
                        }
                    }
            )

        }


    }
}
