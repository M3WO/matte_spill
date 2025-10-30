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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mappe1_s371428.R
import com.example.mappe1_s371428.SpillViewModel
import com.example.mappe1_s371428.ui.theme.AppTheme
import com.example.mappe1_s371428.ui.theme.onPrimaryContainerLight
import com.example.mappe1_s371428.ui.theme.primaryLight

// Composable funksjon for Preferanse siden i matte applikasjonen. Lar brukere valge antell regnestykker per spilløkt.
@Composable
fun PrefSide(
    modifier: Modifier = Modifier, // Benyttes for justering av layout for denne composablen.
    spillViewModel: SpillViewModel, // For mulighet til å holde styr på spilltilstand og preferanser.
    vedNavigasjonTilbake: () -> Unit // Callback funksjon som kalles når bruker vil navigere tilbake fra preferanse siden.
) {
    // Observerer endringer i antall valgte regnestykker fra SpillViewModel. Når verdien endres, oppdateres UI'en.
    val antallUtregninger by spillViewModel.antallUtregninger.collectAsState()

    // Hovedkolonne for hele skjermen for preferanse siden.
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF4AAED6)) // Lyse blå bakgrunnsfarge for hele siden.
    ) {

        // En avstand for å posisjonere "Lagre"-knappen lenger ned på skjermen
        Spacer(modifier = Modifier.height(400.dp))

        // Knapp oppsett for "Lagre"-knapp for å lagre preferanser og navigere tilbake
        Button(
            onClick = vedNavigasjonTilbake, // Callback som kalles ved klikk.
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF64C55D), // Grønn farge for "Lagre"-knappen.
                contentColor = Color(0xFFFFEEC7) // Gul kremfarge for tekst/ikon.
            ),
            modifier = modifier.size(width = 200.dp, height = 60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(R.string.lagre_knapp), // "Lagre"-knapp tekst strengen hentet fra strings.xml
                    fontSize = 32.sp
                )
                Spacer(Modifier.width(8.dp))
                // Ikon som visuelt representerer "Lagre".
                Icon(
                    painter = painterResource(id = R.drawable.arrow_circle_down_svgrepo_com),
                    contentDescription = stringResource(R.string.lagre_knapp),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }

    // Kolonne for å visning av tittel-kortet for "Preferanser" plasser øverst på preferanse siden.
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(270.dp)) // For å plassere kortet nærmere resten av innholdet (ned fra toppen).

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(32.dp),
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
                    // Ikon for å representere "Preferanser" eller instillinger.
                    Icon(
                        painter = painterResource(id = R.drawable.gear_svgrepo_com), // Tannhjul (innstillings) ikon
                        contentDescription = stringResource(R.string.preferanser_tittel),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(id=R.string.preferanser_tittel), // "Preferanser"-tittel tekst.
                        fontSize = 40.sp
                    )
                }
            }
        }
    }

    // Kolonne for å vise valg av antall oppgaver.
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(380.dp)) // Posisjon som plasserer kortet mitt på skjermen

        Card(
            modifier = modifier
                .clip(RoundedCornerShape(32.dp)) // Runde hjørner for kortet
                .background(Color(0xFFFFEEC7)) // Gul kremfarge for bakgrunnsfarge på kortet
                .border(width = 6.dp, shape = RoundedCornerShape(32.dp), color = Color(0xFFFBAF03)) // Gul/oransje ramme rundt kortet.
                .padding(36.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.background(Color(0xFFFFEEC7))
            ){
                Text(
                    text = stringResource(R.string.velg_antall_oppgaver), // Denne strengen burde endres litt på // Tekst streng
                    fontSize = 16.sp,
                    color = onPrimaryContainerLight, // Mørke blå tekst farge
                    modifier = modifier.background(Color(0xFFFFEEC7)) // Gul kremfarge for bakgrunnsfargen til teksten.
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Rad med knapper for å velge 5, 10 eller 15 oppgaver.
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ){
                    val knappStorrelse = 85.dp // Standard størrelse for knappene.
                    val valgtKnappStorrelse = 95.dp // Større knapp for trykket knapp. For å la bruker se antall oppgaver valgt
                    val ikkeValgtSkygge = 2.dp // Standard skygge rundt knappene
                    val valgtSkygge = 4.dp // Dypere skygge for valgt knapp

                    // Knapp oppsett for å velge 5 oppgaver.
                    Button(
                        onClick = {
                            spillViewModel.settAntallUtregninger(5) // Oppdaterer SpillViewModel med valgt antall.
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            // Endrer skygge basert på om denne knappen er valgt.
                            defaultElevation = if (antallUtregninger == 5) valgtSkygge else ikkeValgtSkygge
                        ),
                        colors = ButtonDefaults.buttonColors(
                            // Endrer farge for indikasjon om knappen er valgt. Blir lettere for bruker å se antall oppgaver valgt.
                            containerColor = if (antallUtregninger == 5) Color(0xFF45A7DF) else onPrimaryContainerLight,
                            contentColor = Color(0xFFFFEEC7)
                        ),
                        shape = RoundedCornerShape(16.dp), // Runde hjørner på knappen
                        modifier = Modifier
                            .size(
                                // Endrer størrelsen på knappen om knappen er valgt. Knappen blir større hvis valgt.
                                if (antallUtregninger == 5) valgtKnappStorrelse else knappStorrelse
                            )
                    ) {
                        Text(
                            text = "5", // Tekststrengen til knappen. Viser tallet 5
                            fontSize = 32.sp
                        )
                    }

                    // Knapp oppsett for å velge 10 oppgaver.
                    Button(
                        onClick = {
                            spillViewModel.settAntallUtregninger(10) // Oppdaterer SpillViewModel med valgt antall.
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            // Endrer skygge basert på om denne knappen er valgt.
                            defaultElevation = if (antallUtregninger == 10) valgtSkygge else ikkeValgtSkygge
                        ),
                        colors = ButtonDefaults.buttonColors(
                            // Endrer farge for indikasjon om knappen er valgt. Blir lettere for bruker å se antall oppgaver valgt.
                            containerColor = if (antallUtregninger == 10) Color(0xFF45A7DF) else onPrimaryContainerLight,
                            contentColor = Color(0xFFFFEEC7)
                        ),
                        shape = RoundedCornerShape(16.dp), // Runde hjørner på knappen
                        modifier = Modifier
                            .size(
                                // Endrer størrelsen på knappen om knappen er valgt. Knappen blir større hvis valgt.
                                if (antallUtregninger == 10) valgtKnappStorrelse else knappStorrelse
                            )
                    ) {
                        Text(
                            text = "10", // Tekststrengen til knappen. Viser tallet 10
                            fontSize = 32.sp
                        )
                    }

                    // Knapp oppsett for å velge 15 oppgaver.
                    Button(
                        onClick = {
                            spillViewModel.settAntallUtregninger(15) // Oppdaterer SpillViewModel med valgt antall.
                        },
                        elevation = ButtonDefaults.buttonElevation(
                            // Endrer skygge basert på om denne knappen er valgt.
                            defaultElevation = if (antallUtregninger == 15) valgtSkygge else ikkeValgtSkygge
                        ),
                        colors = ButtonDefaults.buttonColors(
                            // Endrer farge for indikasjon om knappen er valgt. Blir lettere for bruker å se antall oppgaver valgt.
                            containerColor = if (antallUtregninger == 15) Color(0xFF45A7DF) else onPrimaryContainerLight,
                            contentColor = Color(0xFFFFEEC7)
                        ),
                        shape = RoundedCornerShape(16.dp), // Runde hjørner på knappen
                        modifier = Modifier
                            .size(
                                // Endrer størrelsen på knappen om knappen er valgt. Knappen blir større hvis valgt.
                                if (antallUtregninger == 15) valgtKnappStorrelse else knappStorrelse
                            )
                    ) {
                        Text(
                            text = "15", // Tekststrengen til knappen. Viser tallet 15
                            fontSize = 32.sp
                        )
                    }
                }
            }
        }
    }
}

// Forhåndsvisning av PrefSide composable
@Preview(showBackground = true)
@Composable
fun PrefSidePreview() {
    AppTheme {
        // Tester med å sette en standardverdi for antall utregninger for å se hvordan siden ser ut etter en knapp er trykket på
        val testSpillViewModel = remember { SpillViewModel() }
        testSpillViewModel.settAntallUtregninger(10) // Setter standard verdi for forhåndsvisning
        PrefSide(
            spillViewModel = testSpillViewModel,
            vedNavigasjonTilbake = {}
        )
    }
}
