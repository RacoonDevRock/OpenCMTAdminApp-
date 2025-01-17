package com.cmt.openctmadminapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cmt.openctmadminapp.model.Routes
import com.cmt.openctmadminapp.ui.detailReport.DetailReportAdminScreen
import com.cmt.openctmadminapp.ui.detailReport.TotalReportAdminScreen
import com.cmt.openctmadminapp.ui.home.HomeAdminScreen
import com.cmt.openctmadminapp.ui.login.LoginAdminScreen
import com.cmt.openctmadminapp.ui.research.ResearchAdminScreen

@Composable
fun AppNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String = Routes.HomeAdminScreen.route,
) {
    NavHost(navController = navController, startDestination = startDestination) {
        addHomeRoute(modifier, navController)
        addLoginRoute(modifier, navController)
        addResearchRoute(modifier, navController)
        addDetailIncidentRoute(modifier, navController)
        addReportRoute(modifier)
    }
}

fun NavGraphBuilder.addHomeRoute(modifier: Modifier, navController: NavHostController) {
    composable(Routes.HomeAdminScreen.route) {
        HomeAdminScreen(modifier = modifier, navigationController = navController)
    }
}

fun NavGraphBuilder.addLoginRoute(modifier: Modifier, navController: NavHostController) {
    composable(Routes.LoginAdminScreen.route) {
        LoginAdminScreen(modifier = modifier, navigationController = navController)
    }
}

fun NavGraphBuilder.addResearchRoute(modifier: Modifier, navController: NavHostController) {
    composable(Routes.ResearchAdminScreen.route) {
        ResearchAdminScreen(modifier = modifier, navigationController = navController)
    }
}

fun NavGraphBuilder.addDetailIncidentRoute(modifier: Modifier, navController: NavHostController) {
    composable(Routes.DetailReportAdminScreen.route) {
        DetailReportAdminScreen(modifier = modifier, navigationController = navController)
    }
}

fun NavGraphBuilder.addReportRoute(modifier: Modifier) {
    composable(Routes.TotalReportAdminScreen.route) {
        TotalReportAdminScreen(modifier = modifier)
    }
}
