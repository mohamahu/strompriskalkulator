package com.example.team38

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.team38.screens.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "home"
    ) {
        composable("strompris") {
            StromprisScreen(
                onNavigateToInstillinger = { navController.navigate("instillinger") },
                onNavigateToResultat = {navController.navigate("resultat")},
                onNavigateToOmOss = {navController.navigate("omoss")},
                onNavigateToHome = {navController.navigate("home")}
            )
        }
        composable("instillinger") {
            InstillingerScreen(
                onNavigateToResultat = { navController.navigate("resultat") },
                onNavigateToStrompris = {navController.navigate("strompris")},
                onNavigateToOmOss = {navController.navigate("omoss")},
                onNavigateToHome = {navController.navigate("home")}

            )
        }
        composable("resultat"){
            ResultatScreen(
                onNavigateToStrompris = {navController.navigate("strompris")},
                onNavigateToInstillinger = {navController.navigate("instillinger")},
                onNavigateToOmOss = {navController.navigate("omoss")},
                onNavigateToHome = {navController.navigate("home")}
            )
        }
        composable("omoss"){
            OmOss(
                onNavigateToStrompris = {navController.navigate("strompris")},
                onNavigateToInstillinger = {navController.navigate("instillinger")},
                onNavigateToResultat = {navController.navigate("resultat")},
                onNavigateToHome = {navController.navigate("home")}
            )
        }
        composable("home"){
            HomeScreen(
                onNavigateToInstillinger = { navController.navigate("instillinger") },
                onNavigateToStrompris = { navController.navigate("strompris") },
                onNavigateToResultat = { navController.navigate("resultat") },
                onNavigateToOmOss = {navController.navigate("omoss")})
        }
    }
}
