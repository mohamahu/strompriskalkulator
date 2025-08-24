package com.example.team38.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstillingerScreen(onNavigateToResultat: () -> (Unit), onNavigateToStrompris: () -> (Unit), onNavigateToOmOss: () -> (Unit), onNavigateToHome: () -> Unit){
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val icons = listOf(Icons.Default.Home, Icons.Default.LocationOn, Icons.Default.Settings, Icons.Default.Search, Icons.Default.Info)
    val items = listOf("Home", "Stømpriser", "Instillinger", "Resultat", "Om oss")
    val selectedItem = remember { mutableStateOf(icons[2]) }
    val itemsWithIcons = icons.zip(items)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet{
                Spacer(Modifier.height(12.dp))
                itemsWithIcons.forEach{(icon, label)->
                    NavigationDrawerItem(
                        icon = { Icon(icon, contentDescription = null) },
                        label = {Text(label)},
                        selected = icon == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close()}
                            selectedItem.value = icon
                            when(icon) {
                                Icons.Default.Home -> onNavigateToHome()
                                Icons.Default.LocationOn -> onNavigateToStrompris()
                                Icons.Default.Search -> onNavigateToResultat()
                                Icons.Default.Info -> onNavigateToOmOss()
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
                title = {Text(text = "Resultat")},

                navigationIcon = {
                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .fillMaxSize()
                    ) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    }
                },



                )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
                    .padding(top = 50.dp)
                    .background(color = Color(0xFFEBEDFF)),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ){
                val switchState = remember {mutableStateOf(false)}

                Text("Neste dag", color = Color.Black)
                Box(modifier = Modifier.border(border = BorderStroke(3.dp, Color.Black),
                    shape = MaterialTheme.shapes.small).padding(8.dp)) {
                    Switch(
                        checked = switchState.value,
                        { switchState.value = it },
                        Modifier.padding(0.dp)
                    )
                }
                Box(contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.fillMaxHeight().fillMaxWidth()){

                    Button(onClick = onNavigateToOmOss, modifier = Modifier.fillMaxWidth().align(Alignment.CenterStart)) {

                        Text(text = ("Om oss"), color = Color.Black, fontSize = 20.sp)

                    }
                }
            }
        }
    )

}
