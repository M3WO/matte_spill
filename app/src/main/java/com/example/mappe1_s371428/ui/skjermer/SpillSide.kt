package com.example.mappe1_s371428.ui.skjermer

import android.graphics.Paint
import android.widget.Button
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
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

// Composable funksjon for spillsiden hvor brukere spiller spillet og løser matte rgenestykker
@Composable
fun SpillSide(
    modifier: Modifier = Modifier, // For endring av oppsett av utseendet eller oppførselen til denne komponenten.
    spillViewModel: SpillViewModel, // For mulighet til å legge inn spilllogikken og tilstanden i spillet
    vedNavigasjonTilbake: () -> Unit // Lambda-funksjon som kalles når brukes skal navigere tilbake til startskjerm
) {

    // Variabler for observering og henting av gjeldende stilstand fra SpillViewModel for å oppdatere utseendet (UI)
    val gjeldendeUtregningTekst by spillViewModel.gjeldendeUtregningTekst.collectAsState()
    val spillerSvar by spillViewModel.spillerSvar.collectAsState()
    val gjeldendeUtregningIndeks by spillViewModel.gjeldendeUtregningIndeks.collectAsState()
    val totalUtregningerISesjon by spillViewModel.gjeldendeSesjonUtregninger.collectAsState()
    val spillFerdig by spillViewModel.spillFerdig.collectAsState()

    // Lokal tilstand for å holde styr på synligheten av "Avslutt spill"-dialogen når bruker trykker på hjem knappen i spill sesjonen
    var visAvsluttDialog by remember { mutableStateOf(false)}

    // Tilstander for dialogbokser for tilbakemelding på svar
    val visFeilSvarDialog by spillViewModel.visFeilSvarDialog.collectAsState()
    val visRiktigSvarDialog by spillViewModel.visRiktigSvarDialog.collectAsState()
    val dialogMelding by spillViewModel.dialogMelding.collectAsState()

    // Håndterer tilbakeknappen på selve enheten (mobil). H
    BackHandler {
        // Hvis spillet ikke er ferdig, vises det en bekreftelsesdialog for valg om å avslutte spill
        if (!spillFerdig) {
            visAvsluttDialog = true // Vis dialog for å bekrefte avslutning
        } else {
            spillViewModel.avsluttSpill() //Nullstill SpillViewModel tilstand
            vedNavigasjonTilbake() // Navigerer tilbake til startskjermen
        }
    }

    // Hoved kolonne som dekker hele skjermen for spill siden
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize() // Fyller hele skjermen
            .background(Color(0xFF4AAED6)) //Lys blå bagrunnsarge
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Rad for Hjem knapp og Score oversikt
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth() // Fyller hele bredden av raden
        ){
            // Lager "Hjem"-knapp hvis ønskelig om å avslutte spill (øverst til venstre)
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                // "Hjem" knappen er kun synlig hvis bruker ikke er ferdig med spillet
                if (!spillFerdig) {
                    Button(
                        onClick = { visAvsluttDialog = true }, // Setter state/flagg for å vise "avslutt spill?" dialogboksen
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = onPrimaryContainerLight, // Mørke/Navy blå farge på knappen
                            contentColor = Color(0xFFFFEEC7) // lys gul/krem farge for hjem-ikon
                        ),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier.size(70.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.house_chimney_blank_svgrepo_com), // "Hjem" ikon
                            contentDescription = stringResource(R.string.avslutt_spill_knapp), // Beskrivelse på ikon
                            modifier = Modifier.size(70.dp) // Størrelse på ikon
                        )
                    }
                }
            }

            // Score/fremdriftsindikator f.eks 3/5 (indikerer totale oppgaver og antall oppgaver utført). Vises øverst til høyre
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopEnd // Plasserer score boksen i øvre høyre hjørne
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = onPrimaryContainerLight, // Mørke blå kort farge
                        contentColor = Color(0xFFFDC032) // oransje gul tekst
                    ),
                    modifier = modifier
                        .height(70.dp)
                        .width(139.dp)
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(28.dp),
                        )
                        .border(width = 4.dp, shape = RoundedCornerShape(28.dp), color = Color(0xFFFDC032)) // Oransje gul ramme rundt score boksen
                ) {
                    // Tekst som viser gjeldende oppgavenummer og totalt antall spørsmål (f.eks "3/5"). Verdier hentet fra SpillViewModel
                    Text(
                        text = " ${gjeldendeUtregningIndeks + 1}/${totalUtregningerISesjon.size}",
                        fontSize = 37.sp,
                        modifier = Modifier
                            .padding(14.dp)
                            .align(Alignment.CenterHorizontally) // Sentrerer tekst horisontalt
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        // Kort som viser regnestykkene og brukers svar (Satt opp for å ligne på en tavle)
        Card(
            modifier = modifier
                .clip(RoundedCornerShape(32.dp)) // Runde hjørner
                .background(Color(0xFFFFEEC7)) // Bakgrunnsfarge på selve kortet gul kremfarge
                .border(width = 6.dp, shape = RoundedCornerShape(32.dp), color = Color(0xFFFBAF03)) // Ramme med oransje aktig farge
                .padding(26.dp)
        ) {
            Text(
                text = "$gjeldendeUtregningTekst = $spillerSvar", // Viser nåværende oppgave og spillers svar
                fontSize = 80.sp,
                color = onPrimaryContainerLight, // Mørkeblå tekst
                modifier = modifier.background(Color(0xFFFFEEC7)) // Legger til bakgrunnsfarge til krem gul farge, slik at det passer bakgrunnsfargen på selve kortet
            )
        }
    }

    // Kolonne for taste knappene, "fjern siste siffer"-knapp og "Sjekk svar"-knapp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(375.dp)) // Plasserer knappene under der regnstykkene vises

        // Rad 1: Tallknapper 7, 8, 9
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp), // Avstand mellom knappene på raden
        ){
            NummerKnapp(text = "7", onClick = { spillViewModel.leggTilISvar("7") })
            NummerKnapp(text = "8", onClick = { spillViewModel.leggTilISvar("8") })
            NummerKnapp(text = "9", onClick = { spillViewModel.leggTilISvar("9") })
        }

        Spacer(modifier = Modifier.height(16.dp)) // Avstand mellom knappene fra 1. rad til 2. rad

        // Rad 2: Tallknapper 4, 5, 6
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ){
            NummerKnapp(text = "4", onClick = { spillViewModel.leggTilISvar("4") })
            NummerKnapp(text = "5", onClick = { spillViewModel.leggTilISvar("5") })
            NummerKnapp(text = "6", onClick = { spillViewModel.leggTilISvar("6") })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rad 2: Tallknapper 1, 2, 3
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ){
            NummerKnapp(text = "1", onClick = { spillViewModel.leggTilISvar("1") })
            NummerKnapp(text = "2", onClick = { spillViewModel.leggTilISvar("2") })
            NummerKnapp(text = "3", onClick = { spillViewModel.leggTilISvar("3") })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rad 4: "Fjern siste siffer"-knapp, "0"-knapp og en tom plassholder
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ){
            // Oppsett for "Fjern siste siffer"-knapp
            Button(
                onClick = { spillViewModel.fjernSisteSifferFraSvar() }, // Funskjon fra SpillViewModel som fjerner siste siffer kalles ved klikk
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = onPrimaryContainerLight, // Mørke blå knapp farge
                    contentColor = Color(0xFFFFEEC7) // Krem gul farge for tekst/ikon
                ),
                shape = RoundedCornerShape(16.dp), // Runde hjørner for knappen
                modifier = Modifier.size(85.dp)    // Kvadrat for på knapp
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_right_svgrepo_com), // Tilpasset ikon som symbolyserer at man kan fjerne siffere som ble skrevet inn
                    contentDescription = "Fjern siste tall",
                    modifier = Modifier
                        .size(40.dp)
                        .graphicsLayer(scaleX = -1f) // Vender ikon horisontalt for bedre visuell effekt (Fant ikke noe ikon som pekte den riktige veien)
                )
            }

            // Tall knapp 0
            NummerKnapp(text = "0", onClick = { spillViewModel.leggTilISvar("0") })

            // En tom card som benyttes som en plassholder for å opprettholde utseende på layout
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4AAED6) // Setter bakgrunnsfarge lyse blå på kortet for å skjule kortet, da det ikke skal være noe knapp der
                ),
                shape = RoundedCornerShape(16.dp), // Runde hjørner
                modifier = Modifier.size(85.dp) // Kvadratisk knapp utseende
            ) {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Oppsett for "Sjekk svar"-knapp
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = { spillViewModel.sendInnSvar() }, // Sender svar til SpillViewModel for validering om svar er riktig eller feil
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64C55D), // Grønn knapp farge
                    contentColor = Color(0xFFFFEEC7) // Krem gul farge for tekst/ikon
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier // Bredere størrelse på knapp for å lettere legge merke til den
                    .height(75.dp)
                    .width(303.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Sjekk svar tekst
                    Text(
                        text = stringResource(R.string.sjekk_svar_knapp),
                        fontSize = 32.sp
                    )
                    Spacer(Modifier.width(8.dp))

                    // "Checkmark" ikon
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.sjekk_svar_knapp),
                        modifier = Modifier.size(45.dp)
                    )
                }
            }
        }
    }

    // Dialogboks som vises hvis "Spill ferdig" eller "Avslutt spill". Dialog vises når spill er fullført (spillFerdig er true) eller når bruker manuelt trykker "Hjem"-knappen for å avslutte (visAvsluttDialog er true)
    if (spillFerdig || visAvsluttDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)), // Mørk/Svart transparent bakgrunn
            contentAlignment = Alignment.Center // Sentrerer dialog boks
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEEC7), // gul kremfarge bakgrunnsfarge for dialogboksene
                    contentColor = onPrimaryContainerLight // Mørke blå tekst som standard tekst farge i dialogboksene
                ),
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp)
                    .border(width = 4.dp, shape = RoundedCornerShape(24.dp), color = Color(0xFFFBAF03)) // Oransje ramme
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (spillFerdig) stringResource(R.string.spill_ferdig_tittel) else
                            stringResource(R.string.avslutt_spill_dialog_tittel), // Dynamisk tittel basert på spilltilstand (Tittel som vises for om spill er ferdig eller om du velger å avslutte spill)
                        fontSize = 24.sp,
                        color = onPrimaryContainerLight // Mørke blå tekst farge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (spillFerdig) stringResource(R.string.spill_ferdig_melding) else
                            stringResource(R.string.avslutt_spill_dialog_melding), // Dynamisk melding dialog basert på om spill er ferdig eller om du ønsker å avslutte spill
                        fontSize = 18.sp,
                        color = onPrimaryContainerLight // Mørke blå tekst farge
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    // Knapp oppsett for å starte et nytt spill
                    Button(
                        onClick = {
                            spillViewModel.startSpillPoNytt() // Kaller på funksjon fra SpillViewModel som lar bruker starte nytt spill når knappen er klikket på
                            visAvsluttDialog = false // skjuler dialog boksen som vistes tidligere når det ble spørsmål om å avslutte spill
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF64C55D), // Grønn knapp farge
                            contentColor = Color(0xFFFFEEC7)  // gul kremfarge for tekste/ikon
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.start_paa_nytt_knapp), fontSize = 20.sp) // "Start på nytt"-knapp tekst
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    // Knapp oppsett for å avslutte spill og nagivere tilbake til start skjerm
                    Button(
                        onClick = {
                            spillViewModel.restartSpill() // Nullstiller SpillViewModel ved klikk
                            vedNavigasjonTilbake() //Naviger tilbake til StartSkjerm ved klikk
                            visAvsluttDialog = false // Skjuler dialog ved klikk
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = onPrimaryContainerLight, // Mørke blå knapp farge
                            contentColor = Color(0xFFFFEEC7) // Gul kremfarge for tekst/ikon
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.avslutt_knapp), fontSize = 20.sp) // "Avslutt"-knapp tekst
                    }

                    // "Fortsett"-knappen vises kun hvis spillet ikke er ferdig og bruker ønsket i utgangspunktet å avslutte spill ved å klikke på "Hjem"-knappen
                    if (!spillFerdig) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { visAvsluttDialog = false }, // Lukk dialogen og fortsett spillet
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF45A7DF), // Blå knapp farge
                                contentColor = Color(0xFFFFEEC7) // Gul kremfarge for tekst/ikon
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.fortsett_spill_knapp), fontSize = 20.sp) // "Fortsett spill"-knapp tekst
                        }
                    }
                }
            }
        }
    }

    // Dialogboks som vises hvis bruker gir et feil svar.
    if (visFeilSvarDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEEC7), // Gul kremfarge bakgrunnsfarge for dialogboks
                    contentColor = Color(0xFFFA5725) // Rød farge på tekst/ikon for indikasjon på at svar er feil
                ),
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp)
                    .border(width = 4.dp, shape = RoundedCornerShape(24.dp), color = Color(0xFFFA5725)) // Rød ramme rundt dialogboksen for indikasjon på at svar er feil
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = stringResource(R.string.feil_svar_dialog_tittel), // "Feil svar"-tekst for dialog tittel
                            fontSize = 24.sp,
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.circle_xmark_svgrepo_com), // X-ikon ved siden av tittel for indikasjon på at svar er feil
                            contentDescription = stringResource(R.string.feil_svar_dialog_tittel),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = dialogMelding, // Gitt spesifikk feilmelding fra SpillViewModel tekst
                        fontSize = 18.sp,
                        color = onPrimaryContainerLight // Mørke blå tekst farge
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // "Prøv igjen"-knapp oppsett
                    Button(
                        onClick = {
                            spillViewModel.lukkFeilSvarDialog() // Skjuler dialogen og tømmer svar ved klikk
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF64C55D), //Grønn farge knapp
                            contentColor = Color(0xFFFFEEC7) // Gul kremfarge for tekst/ikon
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.prov_igjen_knapp), fontSize = 20.sp) // "Prøv igjen"-knapp tekst
                    }
                }
            }
        }
    }

    // Dialogboks som vises når bruker gir et riktig svar
    if (visRiktigSvarDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)), // Mørk/Svart transparent bakgrunn
            contentAlignment = Alignment.Center,
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEEC7), // Gul kremfarge bakgrunnsfarge for dialogboks
                    contentColor = Color(0xFF64C55D) // Grønn farge på tekst/ikon for indikasjon på at svar er riktig
                ),
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp)
                    .border(width = 4.dp, shape = RoundedCornerShape(24.dp), color = Color(0xFF64C55D)) // Grønn ramme
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = stringResource(R.string.riktig_svar_dialog_tittel), // "Riktig svar"-tekst for dialog tittel
                            fontSize = 24.sp,
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.circle_check_svgrepo_com), // Checkmark ikon ved siden av tittel for indikasjon på at svar er riktig
                            contentDescription = stringResource(R.string.riktig_svar_dialog_tittel),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = dialogMelding, // Gitt spesifikk riktig-melding fra SpillViewModel tekst
                        fontSize = 18.sp,
                        color = onPrimaryContainerLight // Mørke blå tekst farge
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // "Neste oppgave"-knapp oppsett
                    Button(
                        onClick = {
                            spillViewModel.lukkRiktigSvarDialogOgNesteOppgave()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF45A7DF), // Lyse blå farge knapp
                            contentColor = Color(0xFFFFEEC7) // Gul kremfarge for tekst/ikon
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.neste_oppgave_knapp), fontSize = 20.sp) // "Neste oppgave"-knapp tekst
                    }
                }
            }
        }
    }
}

// Composable funksjon for å lettere organisere tallknappene
@Composable
fun NummerKnapp(
    text: String, // Tekst som skal vises på knappen (f.eks "2").
    onClick: () -> Unit, // Lambda-funksjon som kalles når knappen trykkes.
    modifier: Modifier = Modifier, // For endring av utseende eller oppførsel
    color: Color = Color(0xFFFDC032), // Standard farge for knapp er satt til oransje/gul
    textColor: Color = onPrimaryContainerLight // Standard tekst farge på knappen er satt til mørke blå
) {
    Button(
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(16.dp), // Runde hjørner
        modifier = modifier.size(85.dp) // Kvadratisk form
    ) {
        Text(
            text = text, // Her legges tekst inn, altså tallene i streng form
            fontSize = 52.sp
        )
    }
}

// Forhåndsvisning av StartSide composable
@Preview(showBackground = true)
@Composable
fun SpillSidePreview() {
    AppTheme {
        // Oppretter en test SpillViewModel for å simulere sppilltilstand i forhåndsvisning
        val testSpillViewModel = remember { SpillViewModel() }
        // Setter noen dummy-data for å se hvordan det er å se innhold i preview
        testSpillViewModel.settAlleUtregninger(listOf("1 + 1" to "2", "2 + 2" to "4"))
        testSpillViewModel.startNyttSpillSesjon()

        SpillSide(
            spillViewModel = testSpillViewModel,
            vedNavigasjonTilbake = {} // Satter en tom lambda da navigasjon ikke skjer i preview
        )
    }
}
