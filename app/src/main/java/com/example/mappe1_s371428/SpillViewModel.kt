package com.example.mappe1_s371428

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//ViewModel for spilllogikken i matteappen. Håndterer spilltilstand, brukerinteraksjon og lagring/henting av preferanser (SharedPreferences)
class SpillViewModel(private val applicationContext: Context? = null) : ViewModel() {

    // Konstanter for SharedPreferences
    private val PREFS_NAVN = "MatteSpillPrefs" // Navn på SharedPreferences-filen
    private val NOKKEL_ANTALL_UTREGNINGER = "antallUtregninger" // Nøkkel for å lagre antall regnestykker per spill sesjon

    // SharedPreferences-instansen for å lagre og hente brukerpreferanser
    private val prefs: SharedPreferences? = applicationContext?.getSharedPreferences(PREFS_NAVN, Context.MODE_PRIVATE)

    // Holder styr på antall regnestykker og observere endringer i antall regnestykker per spill. Standardverdier hentes fra preferanser og hodler styr
    private val _antallUtregninger = MutableStateFlow(hentAntallUtregningerFraPrefs())
    val antallUtregninger: StateFlow<Int> = _antallUtregninger.asStateFlow()

    // Liste som holder alle tilgjengelige regnestykker og deres svar (f.eks. "2+2" til "4").
    private var _alleUtregninger: List<Pair<String, String>> = emptyList() // Liste over utregninger

    // Liste over regnestykker som benyttes i den nåværende spillsesjonen og observere regnestykker i gjeldende sesjon for UI
    private val _gjeldendeSesjonUtregninger =
        MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val gjeldendeSesjonUtregninger: StateFlow<List<Pair<String, String>>> = _gjeldendeSesjonUtregninger.asStateFlow()

    // Indeks for det nåværende regnestykket i _gjeldendeSesjonUtregninger (sesjon)
    private val _gjeldendeUtregningIndeks =
        MutableStateFlow(0)
    val gjeldendeUtregningIndeks: StateFlow<Int> = _gjeldendeUtregningIndeks.asStateFlow()

    // Streng som holder det bruker har tastet inn som svar på gjeldende regnestykke
    private val _spillerSvar = MutableStateFlow("")
    val spillerSvar: StateFlow<String> = _spillerSvar.asStateFlow()

    // Indikasjon om hele spillsesjonen er fullført. Benytter Boolean
    private val _spillFerdig = MutableStateFlow(false)
    val spillFerdig: StateFlow<Boolean> = _spillFerdig.asStateFlow()

    // Boolean som kontrollerer synligheten av en dialogboks for feil svar.
    private val _visFeilSvarDialog = MutableStateFlow(false)
    val visFeilSvarDialog: StateFlow<Boolean> = _visFeilSvarDialog.asStateFlow()

    // Boolean som kontrollerer synligheten av en dialogboks for riktig svar.
    private val _visRiktigSvarDialog = MutableStateFlow(false)
    val visRiktigSvarDialog: StateFlow<Boolean> = _visRiktigSvarDialog.asStateFlow()

    // Liste med varierende tilbakemeldinger ved feil svar
    private val feilTilbakemeldinger = listOf(
        R.string.feil_tilbakemelding_1,
        R.string.feil_tilbakemelding_2,
        R.string.feil_tilbakemelding_3,
        R.string.feil_tilbakemelding_4,
    )

    // Liste med varierende tilbakemeldinger ved riktig svar
    private val riktigTilbakemeldinger = listOf(
        R.string.riktig_tilbakemelding_1,
        R.string.riktig_tilbakemelding_2,
        R.string.riktig_tilbakemelding_3,
        R.string.riktig_tilbakemelding_4,
    )

    // Streng som holder den spesifikke meldingen som skal vises i en dialogboks (f.eks "kjempe bra!").
    private val _dialogMelding = MutableStateFlow("")
    val dialogMelding: StateFlow<String> = _dialogMelding.asStateFlow()

    // Kombinerer gjeldende utregningsindeks og listen over sesjonsutregninger for å dynamisk gi teksten til det aktuelle regnestykket som skal vises i UI
    val gjeldendeUtregningTekst: StateFlow<String> = combine(
        _gjeldendeUtregningIndeks,
        _gjeldendeSesjonUtregninger
    ) { indeks, utregninger ->
        // Returnerer en standardmelding som vises hvis det er ingen spørsmål tilgjengelige.
        utregninger.getOrNull(indeks)?.first ?: (applicationContext?.getString(R.string.ingen_sporsmal) ?:"Ingen spørsmål")
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), // Starter når det er observatører, hold aktiv i 5 sekunder etter den siste
        applicationContext?.getString(R.string.ingen_sporsmal) ?: "Laster..." // Initialverdi før data er klar
    )

    init {
        // Initialiserer ny spillsesjonen når ViewModel-instansen opprettes. Sikrer at spillet er klart når bruker navigerer til Spill siden.
        startNyttSpillSesjon()
    }

    // Funksjon som lagrer det gitte antallet regnestykker per spill til SharedPreferences
    private fun lagreAntallUtregningerTilPrefs(antall: Int) {
        prefs?.edit()?.putInt(NOKKEL_ANTALL_UTREGNINGER, antall)?.apply()
    }

    // Henter antall regnestykker per spill fra SharedPreferences
    private fun hentAntallUtregningerFraPrefs(): Int {
        return prefs?.getInt(NOKKEL_ANTALL_UTREGNINGER, 5) ?: 5// Standardverdi er 5
    }

    // Setter den komplette listen over alle tilgjengelige regnestykker.
    fun settAlleUtregninger(utregninger: List<Pair<String, String>>) {
        _alleUtregninger = utregninger
    }

    // Setter antall regnestykker som skal spilles per sesjon og lagrer dette til SharedPreferences
    fun settAntallUtregninger(antall: Int) {
        _antallUtregninger.value = antall
        lagreAntallUtregningerTilPrefs(antall) // Lagre endringen til SharedPreferences
    }

    // Funksjon som initaliserer en ny spillsesjon, velger tilfeldige regnestykker og tilbakestiller spilltilstanden
    fun startNyttSpillSesjon() {
        viewModelScope.launch {
            if (_alleUtregninger.isNotEmpty()) {
                val blandedeUtregninger = _alleUtregninger.shuffled() // Stokker om alle tilgjengelige regnestykker
                _gjeldendeSesjonUtregninger.value = blandedeUtregninger.take(_antallUtregninger.value) // Velger at antall regnestykker for den nye sesjonen
                _gjeldendeUtregningIndeks.value = 0 // startindeks peker på første regnestykke (starter med første regnestykke)
                _spillerSvar.value = "" // Tømmer brukers svarfelt
                _spillFerdig.value = false // Sikrer at spillet ikke er markert som ferdig
                _visFeilSvarDialog.value = false // Skjuler dialoger
                _visRiktigSvarDialog.value = false // Skjuler dialoger
            }
        }
    }

    // Funksjon som lar bruker taste inn siffer. Maks siffer som kan tastes inn er 2.
    fun leggTilISvar(siffer: String) {
        if (_spillerSvar.value.length < 2) { // Tillater maks 2 siffer for svaret.
            _spillerSvar.value += siffer
        }
    }

    // Funksjon som fjerner det siste sifferet fra brukers nåværende svar. Lar bruker viske bort svar hvis det ble tastet inn feil siffer ved et uhell.
    fun fjernSisteSifferFraSvar() {
        if (_spillerSvar.value.isNotEmpty()) {
            _spillerSvar.value = _spillerSvar.value.dropLast(1)
        }
    }

    // Funksjon som behandler brukers innsendte svar. Sjekker om svar er riktig og oppdaterer spilltilstanden deretter. Viser enten "riktig svar"-dialog eller en "feil svar"-dialog. Håndterer også overgang til neste oppgave eller fullføring av spillet.
    fun sendInnSvar() {
        viewModelScope.launch {
            val gjeldendeUtregning = _gjeldendeSesjonUtregninger.value.getOrNull(_gjeldendeUtregningIndeks.value)
            if ( gjeldendeUtregning != null ) {
                val riktigSvar = gjeldendeUtregning.second

                // Viser "riktig svar"-dialog hvis svar er riktig
                if (_spillerSvar.value == riktigSvar) {
                    _dialogMelding.value = applicationContext?.getString(riktigTilbakemeldinger.random()) ?: "" // Velger tilfeldig positiv tilbakemelding for dialogen

                    // Spill går rett til ferdig spill dialog, og markeres som fullført hvis bruker fullførte siste oppgave
                    val erSisteOppgave = _gjeldendeUtregningIndeks.value == _gjeldendeSesjonUtregninger.value.size - 1
                    if (erSisteOppgave) {
                        // Hopp rett til ferdig-dialog hvis siste oppgave ble fullført
                        _spillFerdig.value = true
                        _visRiktigSvarDialog.value = false // Skjuler riktig dialog, siden ferdig-dialog tar over.
                    } else {
                        // Hvis det er flere oppgaver, vis "riktig svar"-dialog
                        _visRiktigSvarDialog.value = true
                    }

                // Viser "feil svar"-dialog hvis svar er feil
                } else {
                    _dialogMelding.value = applicationContext?.getString(feilTilbakemeldinger.random()) ?: ""   // Velger tilfeldig negativ tilbakemelding for dialog
                    _visFeilSvarDialog.value = true // Vis dialog for feil svar
                }
            }
        }
    }

    // Funksjon som fører bruker til neste regnestykke i spill sesjonen. Hvis alle regnestykker er besvart, er spill markert som ferdig. Tilbakestiller brukers svar og tilbakemeldinger.
    private fun goTilNesteUtregning() {

        //Hvis indeks ikke er mindre enn antall utregninger i gjeldende sesjon - 1. Gå til neste oppgave
        if (_gjeldendeUtregningIndeks.value < _gjeldendeSesjonUtregninger.value.size - 1 ) {
            _gjeldendeUtregningIndeks.value++ // Går til neste indeks
            _spillerSvar.value = "" // Tømmer svarfeltet for den ny utregningen
        } else {
            // Alle regnestykkene i sesjonen er fullført. Spillet er ferdig!
            _spillFerdig.value = true
        }
    }

    // Funksjon som lukker dialogboks etter mottat feil svar tilbakemelding/dialog
    fun lukkFeilSvarDialog() {
        _visFeilSvarDialog.value = false // Skjuler feil svar dialog
        _spillerSvar.value = "" // Tømmer spillers svarfelt
    }

    // Funksjon som lukker dialogboks etter mottat riktig svar tilbakemelding/dialog og går til neste oppgave
    fun lukkRiktigSvarDialogOgNesteOppgave() {
        _visRiktigSvarDialog.value = false // Skjuler riktig svar dialog
        _spillerSvar.value = "" // Tømmer spillers svarfelt
        goTilNesteUtregning() // Gå til neste utregning
    }

    // Funksjon som lar bruker avslutte et pågående spill og tilbakestiller all spillrelatert tilstand.
    fun avsluttSpill() {
        _gjeldendeUtregningIndeks.value = 0 // Tilbakestiller indeks
        _spillerSvar.value = "" // Tømmer brukers svar
        _gjeldendeSesjonUtregninger.value = emptyList() // tømmer utregningsliste for sesjonen
        _spillFerdig.value = false // Sikrer at spillet ikke er markert som ferdig
        _visFeilSvarDialog.value = false // Skjuler eventuelle dialoger
        _visRiktigSvarDialog.value = false // Skjuler eventuelle dialoger
    }

    // Funksjon for å starte spillet på nytt etter fullført sesjon
    fun startSpillPoNytt() {
        _spillFerdig.value = false // Tilbakestill status, slik at UI vet at spillet ikke lenger er ferdig
        startNyttSpillSesjon() // Start en helt ny spillsesjon med nye regnestykker.
    }

    // Funksjon som gir bruker mulighet til å restarte spill sesjonen bruker er i. Resetter hele spilltilstanden til initialverdier
    fun restartSpill() {
        _gjeldendeUtregningIndeks.value = 0 // tilbakestiller indeks
        _spillerSvar.value = "" // Tømmer spillers svar
        _gjeldendeSesjonUtregninger.value = emptyList() // Tøm utregningliste for sesjonen
        _spillFerdig.value = false // Sikrer at spillet ikke er markert som ferdig
        _visFeilSvarDialog.value = false // Skjuler eventuelle dialoger
        _visRiktigSvarDialog.value = false // Skjuler eventuelle dialoger
    }

}