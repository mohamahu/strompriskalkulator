package com.example.team38
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.team38.screens.InstillingerScreen
import com.example.team38.screens.OmOss
import com.example.team38.screens.SkrivTekst
import org.junit.Rule
import org.junit.Test

class AppTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController

    @Test
    fun appNavHost_verifyHome(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                MyAppNavHost(navController = navController)
            }
        }
        composeTestRule.onNodeWithText("Velkommen til VÆRVOLT! \n").assertIsDisplayed()
    }
    @Test
    fun appNavHost_verifyText(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                MyAppNavHost(navController = navController)
            }
        }
        composeTestRule.onNodeWithText("Strømpriser og vær - i én app! Ta smarte valg og spar penger ved å forstå sammenhengen mellom vær og strømpriser")
            .assertIsDisplayed()
    }
    @Test
    fun appNavHost_verifyPicture(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                MyAppNavHost(navController = navController)
            }
        }
        composeTestRule.onNodeWithContentDescription("Bilde av Norges kart").assertIsDisplayed()
    }
    @Test
    fun appNavHost_verifyClick(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                MyAppNavHost(navController = navController)
            }
        }
        composeTestRule.onNodeWithText("Kom i gang!").assertHasClickAction()
    }
    @Test
    fun testContentOmOssScreen() {


        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                MyAppNavHost(navController = navController)
                OmOss(
                    { navController.navigate("home") },
                    { navController.navigate("instillinger") },
                    { navController.navigate("strompriser") },
                    { navController.navigate("resultat") })
            }

        }
        composeTestRule.onNodeWithText("Fem engasjerte studenter ved Universitetet i Oslo har utviklet en app som vil hjelpe folk å spare penger på strømregningen sin. Appen tar hensyn til værforholdene og sammenligner det med strømprisen gjennom dagen.\n")
            .assertIsDisplayed()


        //  assertEquals("com.example.team38", appContext.packageName)
    }
    @Test
    fun testSkrivTekst(){


        composeTestRule.setContent {
            MaterialTheme {
                SkrivTekst()
            }
        }

        composeTestRule.onNodeWithText("Appen viser dagens værdata og strømpris for dagen slik at brukeren kan se sammenhenger mellom disse variablene. På denne måten kan brukeren selv tenke seg hvordan strømprisene vil variere basert på værprognosene.\n")
            .assertExists()

    }
    @Test
    fun testInstillingerScreen(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                MyAppNavHost(navController = navController)
                InstillingerScreen(
                    { navController.navigate("home") },
                    { navController.navigate("strompriser") },
                    { navController.navigate("omoss") },
                    { navController.navigate("resultat") })
            }

        }
        composeTestRule.onNodeWithText("Neste dag").assertIsDisplayed()
    }
    @Test
    fun testSidebar(){
        composeTestRule.setContent {

            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                MyAppNavHost(navController = navController)
                OmOss(
                    { navController.navigate("home") },
                    { navController.navigate("instillinger") },
                    { navController.navigate("strompriser") },
                    { navController.navigate("resultat") })
            }

        }
        composeTestRule.onNodeWithContentDescription("Side bar").assertExists()
    }
    @Test
    fun testSkrollingMotivasjon(){
        composeTestRule.setContent {

            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                MyAppNavHost(navController = navController)
                OmOss(
                    { navController.navigate("home") },
                    { navController.navigate("instillinger") },
                    { navController.navigate("strompriser") },
                    { navController.navigate("resultat") })
            }

        }
        composeTestRule.onNodeWithText("Motivasjon\n").assertIsNotDisplayed()
    }
    @Test
    fun testSkrollTekst(){
        composeTestRule.setContent {

            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
                MyAppNavHost(navController = navController)
                OmOss(
                    { navController.navigate("home") },
                    { navController.navigate("instillinger") },
                    { navController.navigate("strompriser") },
                    { navController.navigate("resultat") })
            }

        }
        composeTestRule.onNodeWithText("'Vi tror at denne appen kan hjelpe folk à ta bedre beslutninger om strømforbruket sitt og dermed spare penger på strømregningen' -Team 38").assertIsNotDisplayed()
    }






}

