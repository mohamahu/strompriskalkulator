package com.example.team38.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.team38.viewModel.StromprisViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultatScreen(stromprisViewModel: StromprisViewModel = StromprisViewModel(), onNavigateToInstillinger: () -> Unit, onNavigateToStrompris: () -> Unit, onNavigateToOmOss: () -> (Unit), onNavigateToHome: () -> Unit){
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val icons = listOf(Icons.Default.Home, Icons.Default.LocationOn, Icons.Default.Settings, Icons.Default.Search, Icons.Default.Info)
    val items = listOf("Home", "Stømpriser", "Instillinger", "Resultat", "Om oss")
    val selectedItem = remember { mutableStateOf(icons[3]) }
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
                title = { Text(text = "Resultat", color = Color.Black) },

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
            //Setter padding slik at det går an å klikke på sidebar
            LazyColumn(modifier = Modifier.padding(top = 50.dp)) {

                item {
                    Column(
                        modifier = Modifier
                            .padding(50.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        val currentDato = LocalDate.now().dayOfMonth
                        val currentMaaned = LocalDate.now().month

                        Text("$currentDato $currentMaaned\nResultater\n", fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black)
                        Text("Temperatur fra idag til imorgen", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 25.dp))
                        VisGraf(viewModel = stromprisViewModel)
                        //Gir rom mellom grafene og dataen slik at det ser bra ut
                        Spacer(Modifier.height(50.dp))
                        Text("Strømpriser i løpet av dagen", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 25.dp))
                        GrafStrompris(viewModel = stromprisViewModel)
                        Spacer(Modifier.height(30.dp))
                        VisData(viewModel = stromprisViewModel)
                    }
                }
            }

        }
    )
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VisGraf(viewModel: StromprisViewModel){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val frostUiState by viewModel.frostUiState.collectAsState()

        //Tar tid før frost data laster inn så får error hvis man ikke tar if-sjekken for tom liste
        if (frostUiState.frost.isNotEmpty()) {
            val data = frostUiState.frost
            val maxY = data.maxOrNull() ?: 0f
            val maxX = data.size - 1

            val startDate = LocalDate.now()
            val endDate = LocalDate.now().plusDays(1)


            val temperatureRange = (data.indices).map { index ->
                val temperature = data[index]
                temperature.toString()
            }

            Canvas(
                modifier = Modifier
                    .height(200.dp)
                    .width(300.dp)

            ) {
                val xInterval = size.width / maxX
                val yInterval = size.height / maxY

                val path = Path()
                path.moveTo(0f, size.height - data.first() * yInterval)

                for (i in 1 until data.size) {
                    val x = i.toFloat() * xInterval
                    val y = size.height - data[i] * yInterval
                    path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(width = 5f)
                )

                val textPaint = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
                textPaint.textSize = 12.sp.toPx()
                textPaint.color = android.graphics.Color.BLACK
                textPaint.isAntiAlias = true
                textPaint.typeface = android.graphics.Typeface.DEFAULT
                textPaint.textAlign = android.graphics.Paint.Align.CENTER

                val stepSize = (data.size / 5).coerceAtLeast(1)
                textPaint.textAlign = android.graphics.Paint.Align.RIGHT
                textPaint.typeface = android.graphics.Typeface.DEFAULT_BOLD

                for (i in data.indices step stepSize) {
                    val x = -8.dp.toPx()
                    val y = size.height - data[i] *yInterval + textPaint.textSize / 2

                    drawContext.canvas.nativeCanvas.drawText(
                        temperatureRange[i],
                        x,
                        y,
                        textPaint
                    )
                }


                val currentDate = endDate.format(DateTimeFormatter.ofPattern("MMM d"))
                val currentX = maxX.toFloat() * xInterval
                val currentY = size.height + 24.dp.toPx()

                drawContext.canvas.nativeCanvas.drawText(
                    currentDate,
                    currentX,
                    currentY,
                    textPaint
                )


                val startDateText = startDate.format(DateTimeFormatter.ofPattern("MMM d"))
                val startX = 0f
                val startY = size.height + 24.dp.toPx()

                drawContext.canvas.nativeCanvas.drawText(
                    startDateText,
                    startX,
                    startY,
                    textPaint
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GrafStrompris(viewModel: StromprisViewModel){

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val stromprisUiState by viewModel.uiState.collectAsState()

        //Tar tid før Strompris data laster inn så får error hvis man ikke tar if-sjekken for tom liste
        if (stromprisUiState.stromPris.isNotEmpty()) {
            val dataInnsamling = stromprisUiState.stromPris
            val data = emptyList<Float>().toMutableList()
            for (i in dataInnsamling){
                data.add(i.NOK_per_kWh.toFloat())
            }
            val maxY = data.maxOrNull() ?: 0f
            val maxX = data.size - 1

            val timeLabels = listOf("00:00", "06:00", "09:00", "12:00", "15:00", "18:00", "23:00")




            val temperatureRange = (data.indices).map { index ->
                val temperature = data[index]
                temperature.toString()
            }

            Canvas(
                modifier = Modifier
                    .height(200.dp)
                    .width(300.dp)

            ) {
                val xInterval = size.width / maxX
                val yInterval = size.height / maxY

                val path = Path()
                path.moveTo(0f, size.height - data.first() * yInterval)

                for (i in 1 until data.size) {
                    val x = i.toFloat() * xInterval
                    val y = size.height - data[i] * yInterval
                    path.lineTo(x, y)
                }

                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(width = 5f)
                )


                val textPaint = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
                textPaint.textSize = 12.sp.toPx()
                textPaint.color = android.graphics.Color.BLACK
                textPaint.isAntiAlias = true
                textPaint.typeface = android.graphics.Typeface.DEFAULT
                textPaint.textAlign = android.graphics.Paint.Align.CENTER


                val stepSize = (data.size / 5).coerceAtLeast(1)

                textPaint.textAlign = android.graphics.Paint.Align.RIGHT
                textPaint.typeface = android.graphics.Typeface.DEFAULT_BOLD

                for (i in data.indices step stepSize) {
                    val x = -8.dp.toPx()
                    val y = size.height - data[i] *yInterval + textPaint.textSize / 2

                    drawContext.canvas.nativeCanvas.drawText(
                        temperatureRange[i],
                        x,
                        y,
                        textPaint
                    )
                }


                val currentDate = timeLabels.last()
                val currentX = maxX.toFloat() * xInterval
                val currentY = size.height + 24.dp.toPx()

                drawContext.canvas.nativeCanvas.drawText(
                    currentDate,
                    currentX,
                    currentY,
                    textPaint
                )
                for (i in data.indices step stepSize) {
                    val x = i.toFloat() * xInterval
                    val y = size.height + 24.dp.toPx()

                    drawContext.canvas.nativeCanvas.drawText(
                        timeLabels[i / stepSize],
                        x,
                        y,
                        textPaint
                    )
                }



                val startDateText = timeLabels.first()
                val startX = 0f
                val startY = size.height + 24.dp.toPx()

                drawContext.canvas.nativeCanvas.drawText(
                    startDateText,
                    startX,
                    startY,
                    textPaint
                )


            }
        }
    }
}
