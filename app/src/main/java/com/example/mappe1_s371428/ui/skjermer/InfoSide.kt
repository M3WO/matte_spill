package com.example.mappe1_s371428.ui.skjermer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mappe1_s371428.MinApp
import com.example.mappe1_s371428.R
import com.example.mappe1_s371428.ui.theme.onPrimaryContainerLight
import com.example.mappe1_s371428.ui.theme.primaryLight

// Composable funksjon som viser en info side til spillet
@Composable
fun InfoSide(
    modifier: Modifier = Modifier, // Benyttes for justering av layout for denne composablen.
    vedNavigasjonTilbake: () -> Unit // Callback funksjon som kalles når bruker vil navigere tilbake fra info siden.
){
    // Hovedkolonne for bakgrunn og toppdel av layout (UI).
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF4AAED6)) // Setter bakgrunnsfarge til lyse blå for hele info siden
    ) {
        Spacer(modifier = Modifier.height(245.dp)) // Setter avstand fra toppen

        // Kort som viser titteln "Om spillet"
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp), // Legger til skyggeeffekt
            shape = RoundedCornerShape(32.dp), // Runde hjørner på kortet
            colors = CardDefaults.cardColors(
                containerColor = onPrimaryContainerLight, // Mørke blå bakgrunnsfarge for kortet.
                contentColor = Color(0xFFFDC032) // Gul farge for tekst/ikon.
            ),
            modifier = modifier
                .size(width = 325.dp, height = 75.dp)
                .shadow(
                    elevation = 7.dp,
                    shape = RoundedCornerShape(32.dp),
                )
                .border(width = 4.dp, shape = RoundedCornerShape(32.dp), color = Color(0xFFFDC032)) // Gul ramme rundt kortet
        ) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Sentrerer innholdet i kortet.
            ) {
                // Rad for Ikon og tekst
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ikon for å representere "Informasjon".
                    Icon(
                        painter = painterResource(id = R.drawable.circle_information_svgrepo_com), // Ikon for informasjon
                        contentDescription = stringResource(R.string.om_spillet_tittel),
                        modifier = Modifier.size(38.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.om_spillet_tittel), // "Om spillet"-tittel tekst.
                        fontSize = 38.sp
                    )
                }
            }
        }
    }

    // Kolonne for hovedinnholdet (informasjonsteksten) sentrert på skjermen.
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        // Kort som inneholder selve informasjonsinnholdet.
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEEC7), // Gul kremfarge bakgrunnsfarge for selve kortet
                contentColor = onPrimaryContainerLight // Mørke blå farge for tekst/ikon
            ),
            modifier = modifier
                .clip(RoundedCornerShape(32.dp)) // Runder av hjørner for kortet.
                .background(Color(0xFFFFEEC7)) // Gul kremfarge for å farge for å fylle opp resterende del av kortet
                .border(width = 6.dp, shape = RoundedCornerShape(32.dp), color = Color(0xFFFBAF03)) // Gul/Oransje farge for ramme rundt kortet
                .padding(32.dp)
        ) {
            // Første rad med ikon og tekst (informasjon om spillet).
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp), // Mellomrom mellom ikon og tekst
            ){
                // Ikon oppsett for "Smile fjes"-ikon
                Icon(
                    painter = painterResource(id = R.drawable.face_smile_svgrepo_com), // "Smile fjes"-ikon for dekorasjon
                    contentDescription = null, // Siden dette er dekorativt, trengs det ikke noe beskrivelse
                    modifier = Modifier.size(30.dp)
                )
                // Tekst oppsett for informasjonstekst
                Text(
                    text = stringResource(R.string.info_tekst_1), // Første informasjonsavsnitt streng
                    fontSize = 16.sp,
                    modifier = modifier.padding(bottom = 16.dp)
                )
            }

            // Andre rad med ikon og tekst (hvordan spille spillet)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp), // Mellomrom mellom ikon og tekst
            ){
                // Ikon oppsett for "Gaming"-ikon
                Icon(
                    painter = painterResource(id = R.drawable.gaming_pad_alt_1_svgrepo_com), // "Gaming"-ikon for dekorasjon og motivasjon for at dette er et spill
                    contentDescription = null, // Siden dette er dekorativt, trengs det ikke noe beskrivelse
                    modifier = Modifier.size(30.dp)
                )
                // Tekst oppsett for informasjonstekst, fokus på hvordan spille spillet.
                Text(
                    text = stringResource(R.string.info_tekst_2), // Andre informasjonsavsnitt streng
                    fontSize = 16.sp,
                    modifier = modifier.padding(bottom = 16.dp)
                )
            }
        }
    }

    // Kolonne for "Hjem"-knappen nederst på skjermen.
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(610.dp)) // For plassering av knapp nederst

        // Knapp oppsett for å gi mulighet til å gå til startskjerm.
        Button(
            onClick = vedNavigasjonTilbake, // Callback som kalles ved klikk.
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = onPrimaryContainerLight, // Mørke blå knapp farge
                contentColor = Color(0xFFFFEEC7) // Krem gul farge for tekst/ikon
            ),
            modifier = modifier.size(width = 200.dp, height = 60.dp)
        ) {
            // Rad for tekst og ikon i knappen
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(R.string.hjem_knapp), // "Hjem"-knapp streng
                    fontSize = 32.sp
                )
                Spacer(Modifier.width(8.dp))
                // Ikon som representerer at man kan gå til startskjerm (Hjem symboliserer start eller hjemme side)
                Icon(
                    painter = painterResource(id = R.drawable.house_chimney_blank_svgrepo_com), // Hjem ikon
                    contentDescription = stringResource(R.string.hjem_knapp),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

// Forhåndsvisning av InfoSide composable
@Preview(showBackground = true)
@Composable
fun InfoSidePreview() {
    InfoSide(vedNavigasjonTilbake = {}) // Holder lambda tom, da det ikke skal skje noe navigering i forhåndsvisningen.
}