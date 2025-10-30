package com.example.mappe1_s371428

//import android.R
import android.R.attr.color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDefaults.color
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mappe1_s371428.ui.theme.AppTheme
import com.example.mappe1_s371428.ui.theme.backgroundDark
import com.example.mappe1_s371428.ui.theme.onPrimaryContainerLight
import com.example.mappe1_s371428.ui.theme.onPrimaryLight
import com.example.mappe1_s371428.ui.theme.primaryContainerLight
import com.example.mappe1_s371428.ui.theme.primaryContainerLightMediumContrast
import com.example.mappe1_s371428.ui.theme.primaryDark
import com.example.mappe1_s371428.ui.theme.primaryLight
import com.example.mappe1_s371428.ui.theme.primaryLightMediumContrast
import com.example.mappe1_s371428.ui.theme.secondaryContainerDarkMediumContrast
import com.example.mappe1_s371428.ui.theme.secondaryDark
import com.example.mappe1_s371428.ui.theme.surfaceLight
import android.content.Context
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mappe1_s371428.R
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mappe1_s371428.SpillViewModel
import com.example.mappe1_s371428.ui.navigasjon.AppNavigasjon
import com.example.mappe1_s371428.ui.skjermer.InfoSide
import com.example.mappe1_s371428.ui.skjermer.PrefSide
import com.example.mappe1_s371428.ui.skjermer.SpillSide
import com.example.mappe1_s371428.ui.skjermer.StartSkjerm

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Anvender appens definerte tema
            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(), // Fyller ut hele skjermen
                    containerColor = Color(0xFF4AAED6), // Setter Scaffold's bakgrunn farge
                ) { innerPadding ->
                    // Henter den lokale konteksten som trengs for å aksessere ressurser og SharedPreferences
                    val kontekst = LocalContext.current

                    // Initialiserer SpillViewModel.
                    // Bruker en VievModelProvider.Factory for å sende 'applicationContext' til ViewModel-konstruktøren
                    val spillViewModel: SpillViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {

                            // Sjekker om forespurte ViewModel-klassen er SpillViewModel
                            if(modelClass.isAssignableFrom(SpillViewModel::class.java)) {
                                @Suppress("UNCHECKED_CAST")
                                // Returnerer en instans av SpillViewModel med applikasjonskonteksten.
                                return SpillViewModel(kontekst.applicationContext) as T
                            }
                            // Kaster unntak hvis en ukjent ViewModel-klasse blir bedt om.
                            throw IllegalArgumentException("Unknown ViewModel class")
                        }

                    })

                    // Laster inn utregninger og svar fra strengtabell under res/values/utregninger.xml
                    val utregninger = kontekst.resources.getStringArray(R.array.matte_utregninger)
                    val svar = kontekst.resources.getStringArray(R.array.matte_svar)

                    // Zipper sammen utregninger og svar til en liste av par
                    val alleUtregninger = utregninger.zip(svar) // Lager liste av Pair<String, String>

                    // Setter de innlastede dataene i SpillViewModellen
                    spillViewModel.settAlleUtregninger(alleUtregninger)

                    // Kaller hoved composable funksjonen for appen
                    MinApp(
                        spillViewModel = spillViewModel,
                        modifier = Modifier.padding(innerPadding) // Benytter padding fra Scaffold
                    )
                }
            }
        }
    }
}

// Hoved composable funksjon for applikasjon
@Composable
fun MinApp(
    spillViewModel: SpillViewModel, // SpillViewModel for å håndtere spilllogikken
    modifier: Modifier = Modifier // Modifier for tilpasse utseendet.
){
    // Håndterer navigasjon i appen ved å bruke AppNavigasjon komponent.
    AppNavigasjon(
        spillViewModel = spillViewModel,
        modifier = Modifier // Hvis behov for utvidelse
    )
}

// Forhåndsvisning av hovedskjermen
@Preview(showBackground = true)
@Composable
fun NewThemeGreetingPreview() {
    AppTheme {
        MinApp(spillViewModel = SpillViewModel())
    }
}