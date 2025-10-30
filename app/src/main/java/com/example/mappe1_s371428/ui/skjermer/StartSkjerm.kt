package com.example.mappe1_s371428.ui.skjermer

//import androidx.compose.foundation.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mappe1_s371428.ui.skjermer.InfoSide
import com.example.mappe1_s371428.ui.skjermer.PrefSide
import com.example.mappe1_s371428.ui.skjermer.SpillSide
import com.example.mappe1_s371428.ui.theme.onPrimaryContainerLight
import com.example.mappe1_s371428.ui.theme.primaryLight
import com.example.mappe1_s371428.R

// Combosable funksjon som representerer startskjermen til applikasjonen
@Composable
fun StartSkjerm(
    modifier: Modifier = Modifier, // Benyttes for å gi mulighet til å endre på layout oppsett (UI)
    vedNavigasjonTilSpill: () -> Unit, // Lambda-funksjon kalles når "Start spill"-knappen trykkes.
    vedNavigasjonTilOm: () -> Unit, // Lambda-funksjon kalles når "Om spillet"-knappen trykkes.
    vedNavigasjonTilPreferanser: () -> Unit // Lamdba-funksjon kalles når "Preferanser"-knappen trykkes

){
    // Hovedkolonne som fyller hele skjermen og sentrerer innholdet vertikalt og horisontalt.
    Column(
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF4AAED6)) // Lyse blå bakgrunn
    ) {
        Spacer(modifier = Modifier.height(155.dp))

        // Bilde oppsett for logo inne på Start skjermen
        Image(
            painter = painterResource(id = R.drawable.matte_spill_app_logo), // Logo bilde over tittel "Matte Spill"
            contentDescription = null,
            modifier = Modifier.size(225.dp)
        )

        // Kort som inneholder appens tittel "Matte spill"
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), // Skygge som gir en liten 3D-effekt.
            shape = RoundedCornerShape(32.dp), // Avrunder kort med hjørner for mykere utseende
            colors = CardDefaults.cardColors(
                containerColor = onPrimaryContainerLight, // Bakgrunnsfarge for kortet. Navy/mørke blå farge
                contentColor = Color(0xFFFDC032) // Farge på teksten eller innholdet generelt inne i kortet. Varm gul/oransje farge
            ),
            modifier = modifier
                .size(width = 325.dp, height = 85.dp) // størrelse for tittelkortet
                .shadow( // ytterlig skygge som ligger rundt kortet
                    elevation = 7.dp,
                    shape = RoundedCornerShape(32.dp),
                )
                .border(width = 4.dp, shape = RoundedCornerShape(32.dp), color = Color(0xFFFDC032)) // Ramme rundt kortet. Farge gul/oransje
        ) {
            // Box for å sentrere teksten "Matte Spill" inne i kortet.
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Tekst som viser appens tittel, hentet fra strings.xml for internasjonalisering
                Text(
                    text = stringResource(R.string.matte_spill_tittel), // Tekste hentet fra strings.xml
                    fontSize = 48.sp, // Tekst størrelse
                )
            }
        }

        // Avstand under tittelkortet for å skille det fra knappene
        Spacer(modifier = Modifier.height(50.dp))

        // Knapp som starter spillet med navn "Start spill".
        Button(
            onClick = vedNavigasjonTilSpill, // Kaller lamdba-funksjon når knappen trykkes.
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp), // Liten skyggeeffekt
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFDC032), // Gul/Oransje bakgrunn
                contentColor = onPrimaryContainerLight // Tekst farge mørke/navy blå
            ),
            modifier = modifier.size(width = 210.dp, height = 70.dp), //Størrelse på knapp

            // Tekst på knapp "Start spill", streng hentet fra strings.xml
            ) {
            Text(
                text = stringResource(R.string.start_spill), // "Start spill"-tittel streng
                fontSize = 20.sp
            )
        }

        // Avstand mellom knappene
        Spacer(modifier = Modifier.height(10.dp))

        // Knapp for å navigere til "Om spillet"-siden
        Button(
            onClick = vedNavigasjonTilOm, // Kaller lambda-funksjon som fører til "Om spillet" siden når knappen trykkes.
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFDC032), // Knapp farge gul/oransje
                contentColor = onPrimaryContainerLight // Mørke blå farge på tekst/ikon
            ),
            modifier = modifier.size(width = 210.dp, height = 70.dp)
        ) {
            // Tekst på knappen.
            Text(
                text = stringResource(R.string.om_spillet), // "Om spillet"-tittel streng
                fontSize = 20.sp
            )
        }

        // Avstand mellom knappene
        Spacer(modifier = Modifier.height(10.dp))

        // Knapp for å navigere til "Preferanser"-siden.
        Button(
            onClick = vedNavigasjonTilPreferanser, // Kaller lambda-funksjon når knappen trykkes.
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFDC032), // Knapp farge gul/oransje
                contentColor = onPrimaryContainerLight // Mørke blå farge på tekst/ikon
            ),
            modifier = modifier.size(width = 210.dp, height = 70.dp)
        ) {
            // Tekst på knappen.
            Text(
                text = stringResource(R.string.preferanser), // "Preferanser"-tittel streng
                fontSize = 20.sp
            )
        }
    }
}

// Forhåndsvisning av StartSkjerm composable
@Preview(showBackground = true)
@Composable
fun StartSkjermPreview() {
    StartSkjerm(
        // Tomme lambda-funksjoner for forhåndsvisning
        vedNavigasjonTilSpill = {},
        vedNavigasjonTilOm = {},
        vedNavigasjonTilPreferanser = {}
    )
}