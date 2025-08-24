package com.example.team38.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.team38.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToInstillinger: () -> Unit, onNavigateToStrompris: () -> Unit, onNavigateToResultat: () -> Unit, onNavigateToOmOss: () -> Unit){
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val icons = listOf(Icons.Default.Home, Icons.Default.LocationOn, Icons.Default.Settings, Icons.Default.Search, Icons.Default.Info)
    val items = listOf("Home", "Stømpriser", "Instillinger", "Resultat", "Om oss")
    val selectedItem = remember { mutableStateOf(icons[0]) }
    val itemsWithIcons = icons.zip(items)
    //Kilde https://commons.wikimedia.org/wiki/File:Norway_counties_blank.svg
    val imageNorge = painterResource(R.drawable.norge)
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
                                Icons.Default.Settings -> onNavigateToInstillinger()
                                Icons.Default.Info -> onNavigateToOmOss()
                                Icons.Default.Search -> onNavigateToResultat()
                                Icons.Default.LocationOn -> onNavigateToStrompris()
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
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    }
                },


                )
            Spacer(Modifier.height(100.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
                    .padding(top = 50.dp)
                    .background(color = Color(0xFFEBEDFF)),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ){
                LazyColumn {
                    item {
                        Text(
                            "Velkommen til VÆRVOLT! \n",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text("Strømpriser og vær - i én app! Ta smarte valg og spar penger ved å forstå sammenhengen mellom vær og strømpriser",
                        color = Color.Black)


                    }
                }
                Box(modifier = Modifier.height(400.dp).fillMaxWidth()) {
                    Image(
                        painter = imageNorge,
                        //Bildebeskrivelse for universell utforming
                        contentDescription = "Bilde av Norges kart",
                        contentScale = ContentScale.Crop
                    )
                }

                Box(contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier.fillMaxHeight().fillMaxWidth()){

                    Button(onClick = onNavigateToStrompris, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(4.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB042FF))) {

                        Text(text = ("Kom i gang!"), color = Color.White)

                    }
                }
            }
        }
    )
}