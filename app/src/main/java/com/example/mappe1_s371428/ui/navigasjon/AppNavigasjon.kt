package com.example.mappe1_s371428.ui.navigasjon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mappe1_s371428.SpillViewModel
import com.example.mappe1_s371428.ui.skjermer.InfoSide
import com.example.mappe1_s371428.ui.skjermer.PrefSide
import com.example.mappe1_s371428.ui.skjermer.SpillSide
import com.example.mappe1_s371428.ui.skjermer.StartSkjerm

// Definerer rutene, altså skjermene i appen ved hjelp av en enum-klasse.
enum class MatteSpillSkjerm() {

    // Hver verdi representerer en unik skjerm i navigasjonsgrafen
    Start, // Startskjermen med hovedmeny.
    Spill, // Selve spillsiden der matte regnestykkene vises og der man spiller
    Om, // Info siden om spillet
    Preferanser, // Siden der man kan velge preferanser for spill
}

// Composable funksjon som setter opp navigasjonsgrafen og holder styr på de forskjellige stedene man kan navigere seg til. Definerer hvilke skjermer/sider som finnes og hvordan man navigerer mellom dem.
@Composable
fun AppNavigasjon(
    spillViewModel: SpillViewModel, // Instans av SpillViewModel for håndtering av spilllogikk og tilstand.
    modifier: Modifier = Modifier, // Benyttes for å justere på layout og utseende (UI)
    navController: NavHostController = rememberNavController() // Instans av NavHostController for å styre navigasjonen. Blir husket automatisk av rememberNavController() hvis ingen er spesifisert.
) {

    // Kjernen i navigasjonen. Her knytter NavHost sammen rutene (routes) og viser de riktige skjermene
    NavHost(
        navController = navController, // Navigasjon som blir styrt av denne kontrolleren
        startDestination = MatteSpillSkjerm.Start.name, // Setter startskjermen for appen start destinasjon når appen kjøres.
        modifier = modifier
    ) {
        // Definerer ruten (route) for "Start"-skjermen.
        composable(route = MatteSpillSkjerm.Start.name) {
            // Navigasjons oppsett for StartSkjerm
            StartSkjerm(
                // Når "Start spill"-knappen trykkes
                vedNavigasjonTilSpill = {
                    spillViewModel.startNyttSpillSesjon() // Kaller på SpillViewModel for å initialisere en ny spillsesjon før navigering
                    navController.navigate(MatteSpillSkjerm.Spill.name) // Kontroller navigerer bruker til spillsiden
                },
                // Når "Om spillet"-knappen trykkes
                vedNavigasjonTilOm = { navController.navigate(MatteSpillSkjerm.Om.name) }, // Navigerer til info side
                // Når "Preferanser"-knappen trykkes
                vedNavigasjonTilPreferanser = { navController.navigate(MatteSpillSkjerm.Preferanser.name)} // Navigerer til Preferanse side
            )
        }

        // Definerer ruten (route) for "Spill"-side/skjerm.
        composable(route = MatteSpillSkjerm.Spill.name) {
            // Navigasjons oppsett for SpillSide
            SpillSide(
                spillViewModel = spillViewModel, // Sender SpillViewModel til spillsiden for aksess til spill-logikken
                // Når alle knapper som fører bruker ut av spillsiden utføres trykkes
                vedNavigasjonTilbake = { navController.navigate(MatteSpillSkjerm.Start.name) {
                    // Fjerner alle destinasjoner opp til Start Skjermen fra back stack.
                    popUpTo(MatteSpillSkjerm.Start.name) { inclusive = true } // Ved inclusive = true fjernes også start skjermen, og blir deretter lagt til på nytt
                } }
            )
        }

        // Definerer ruten (route) for "Info"-side/skjerm.
        composable(route = MatteSpillSkjerm.Om.name) {
            // Navigasjons oppsett for InfoSide
            InfoSide(
                // Når alle knapper som fører bruker ut av om spillet siden (InfoSide) utføres trykkes. Eneste knapp som fører tilbake for bruker er "Lagre" Knappen
                vedNavigasjonTilbake = { navController.popBackStack() }
            )
        }

        // Definerer ruten (route) for "Preferanse"-side/skjerm.
        composable(route = MatteSpillSkjerm.Preferanser.name) {
            // Navigasjons oppsett for PrefSide
            PrefSide(
                // Sender SpillViewModel til Preferanse siden for å lagre og laste inn valgte preferanser
                spillViewModel = spillViewModel,
                // Fører bruker tilnake til startskjerm
                vedNavigasjonTilbake = { navController.popBackStack() }
            )
        }
    }
}