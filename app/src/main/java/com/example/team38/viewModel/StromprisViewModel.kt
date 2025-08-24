package com.example.team38.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.team38.*
import com.example.team38.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class StromprisViewModel : ViewModel(){

    private val strompriser = StromprisData(0.0, 0.0, 0.0, "", "" )
    private val _uiState = MutableStateFlow(StromprisUiState(stromPris = listOf(strompriser)))
    val uiState: StateFlow<StromprisUiState> = _uiState.asStateFlow()


    private val _forecastUiState = MutableStateFlow(ForecastUiState(emptyList()))
    val forecastUiState: StateFlow<ForecastUiState> = _forecastUiState.asStateFlow()

    private val _frostUiState = MutableStateFlow(FrostUiState(emptyList()))
    val frostUiState: StateFlow<FrostUiState> = _frostUiState.asStateFlow()


    //Fikk ikke til å gjøre URL til proxy
    private val baseUrl = "https://hvakosterstrommen.no/api/v1/prices"
    //Alltid satt til Oslo
    private var lat = 59.91
    private var lon = 10.75
    private var prisomraade = "NO1"
    private var dagensDato = Calendar.getInstance()
    private var aaret = dagensDato.get(Calendar.YEAR)
    private var ekteMaaned = dagensDato.get(Calendar.MONTH) + 1 //januar er 0
    private var ekteDag = dagensDato.get(Calendar.DAY_OF_MONTH)
    //Må bruke LocalDate.now().minusDays(1) fordi hvis vi bare tar minus 1 dag kan dagen bli 0
    private var enDagSenere = LocalDate.now().plusDays(1)
    private var aaretEnDagSenere = enDagSenere.year
    private var maaanedenEnDagSenere = enDagSenere.month.value //Henter value slik at vi får nummeret til måneden og ikke selve måned navnet
    private var dagenEnDagSenere = enDagSenere.dayOfMonth
    private var faktiskMaaned = ekteMaaned.toString()
    //Lenken krever at man skal ha 0 foran dagen så vi legger det til hvis dagen er mindre eller lik 9
    private var sjekker = if(faktiskMaaned.toInt() < 9 || faktiskMaaned.toInt() == 9){ "0${ekteMaaned}"}else{ekteMaaned.toString()}
    private var sjekkerDag = if(ekteDag < 9 || ekteDag == 9){ "0${ekteDag}"}else{ekteDag.toString()}
    private var sjekkerEnDagSenere = if(dagenEnDagSenere < 9 || dagenEnDagSenere == 9){ "0${dagenEnDagSenere}"}else{dagenEnDagSenere.toString()}
    private var sjekkerMaanedEnDagSenere = if(maaanedenEnDagSenere < 9 || maaanedenEnDagSenere == 9){ "0${maaanedenEnDagSenere}"}else{maaanedenEnDagSenere.toString()}


    private val baseUrlForecast = "https://gw-uio.intark.uh-it.no/in2000/weatherapi/locationforecast/2.0/complete?lat=$lat&lon=$lon"
    //Satt til MET som er i Oslo
    private val baseUrlFrost = "https://gw-uio.intark.uh-it.no/in2000/frostapi/observations/v0.jsonld?sources=SN18700%3Aall&referencetime=$aaret-${ekteMaaned}-${sjekkerDag}%2F$aaretEnDagSenere-${sjekkerMaanedEnDagSenere}-${sjekkerEnDagSenere}&elements=air_temperature"
    private var dataSource = Datasource(
        "$baseUrl/$aaret/${sjekker}-${ekteDag}_$prisomraade.json",
        baseUrlForecast,
        baseUrlFrost
    )


    init{
        viewModelScope.launch{

            lastInnStrompris()
            lastInnForecast()
            lastInnFrost()
        }
    }
    private fun lastInnStrompris(){
        viewModelScope.launch(Dispatchers.IO){
            val stromPriser = dataSource.fetchStromprisData()
            _uiState.value = StromprisUiState(stromPris = stromPriser)
        }
    }

    private fun lastInnForecast() {
        viewModelScope.launch(Dispatchers.IO) {
            val forecast = dataSource.fetchForecastData()
            _forecastUiState.value = ForecastUiState(forecast)
        }
    }

    private fun lastInnFrost() {
        viewModelScope.launch(Dispatchers.IO) {
            val frost = dataSource.fetchFrostData()
            _frostUiState.value = FrostUiState(frost)
        }
    }
}