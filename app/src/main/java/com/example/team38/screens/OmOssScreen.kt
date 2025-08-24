package com.example.team38.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OmOss(onNavigateToInstillinger: () -> Unit, onNavigateToStrompris: () -> Unit, onNavigateToResultat: () -> Unit, onNavigateToHome: () -> Unit){
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val icons = listOf(Icons.Default.Home, Icons.Default.LocationOn, Icons.Default.Settings, Icons.Default.Search, Icons.Default.Info)
    val items = listOf("Home", "Stømpriser", "Instillinger", "Resultat", "Om oss")
    val selectedItem = remember { mutableStateOf(icons[4]) }
    val itemsWithIcons = icons.zip(items)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet{
                Spacer(Modifier.height(12.dp))
                itemsWithIcons.forEach{(icon, label)->
                    NavigationDrawerItem(
                        icon = { Icon(icon, contentDescription = "Velg skjerm") },
                        label = { Text(label) },
                        selected = icon == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close()}
                            selectedItem.value = icon
                            when(icon) {
                                Icons.Default.LocationOn -> onNavigateToStrompris()
                                Icons.Default.Settings -> onNavigateToInstillinger()
                                Icons.Default.Home -> onNavigateToHome()
                                Icons.Default.Search -> onNavigateToResultat()
                                else -> {}
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            TopAppBar(
                title = { Text(text = "Om oss", color = Color.Black) },

                navigationIcon = {
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .fillMaxSize()
                    ) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Side bar")
                        }
                    }
                },


                )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(65.dp)
                    .background(color = Color(0xFFEBEDFF)),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ){
                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.padding(20.dp))
                        Text(
                            "Om oss \n",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                        Text(
                            "Grunnleggere\n",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            "Fem engasjerte studenter ved Universitetet i Oslo har utviklet en app som vil hjelpe folk å spare penger på strømregningen sin. Appen tar hensyn til værforholdene og sammenligner det med strømprisen gjennom dagen.\n",
                            color = Color.Black
                        )
                        Text(
                            "Om appen\n",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        SkrivTekst()
                        Text(
                            "Motivasjon\n",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            "'Vi tror at denne appen kan hjelpe folk à ta bedre beslutninger om strømforbruket sitt og dermed spare penger på strømregningen' -Team 38",
                            color = Color.Black
                        )
                    }
                }
            }
        }
    )
}
@Composable
fun SkrivTekst(){
    Text(
        "Appen viser dagens værdata og strømpris for dagen slik at brukeren kan se sammenhenger mellom disse variablene. På denne måten kan brukeren selv tenke seg hvordan strømprisene vil variere basert på værprognosene.\n",
        color = Color.Black
    )
}