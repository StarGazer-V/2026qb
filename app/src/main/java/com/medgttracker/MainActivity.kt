package com.medgttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medgttracker.ui.dashboard.DashboardScreen
import com.medgttracker.ui.gt.GtTrackerRoute
import com.medgttracker.ui.profile.ProfileScreen
import com.medgttracker.ui.settings.SettingsScreen
import com.medgttracker.ui.theme.MedGtTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedGtTrackerTheme {
                MedGtTrackerAppNav()
            }
        }
    }
}

@Composable
private fun MedGtTrackerAppNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                onOpenGtTracker = { navController.navigate("gt_tracker") },
                onOpenFlashcards = { navController.navigate("flashcards") },
                onOpenProfile = { navController.navigate("profile") },
                onOpenSettings = { navController.navigate("settings") },
            )
        }
        composable("gt_tracker") { GtTrackerRoute(onBack = navController::popBackStack) }
        composable("flashcards") { SettingsScreen(title = "Flashcards", onBack = navController::popBackStack) }
        composable("profile") { ProfileScreen(onBack = navController::popBackStack) }
        composable("settings") { SettingsScreen(onBack = navController::popBackStack) }
    }
}
