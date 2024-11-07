package com.cmt.openctmadminapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cmt.openctmadminapp.core.ui.home.HomeAdminScreen
import com.cmt.openctmadminapp.login.ui.LoginAdminScreen
import com.cmt.openctmadminapp.report.detail.ui.DetailReportAdminScreen
import com.cmt.openctmadminapp.report.incidentToDetail.ui.TotalReportAdminScreen
import com.cmt.openctmadminapp.research.ui.ResearchAdminScreen

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
        addReportRoute(modifier, navController)
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
    composable(
        route = "${Routes.DetailReportAdminScreen.route}/{nroSolicitud}",
        arguments = listOf(navArgument("nroSolicitud") { type = NavType.StringType })
    ) { backStackEntry ->
        val nroSolicitud = backStackEntry.arguments?.getString("nroSolicitud") ?: ""
        DetailReportAdminScreen(modifier = modifier, navigationController = navController, nroSolicitud = nroSolicitud)
    }
}

fun NavGraphBuilder.addReportRoute(modifier: Modifier, navController: NavHostController) {
    composable(
        route = "${Routes.TotalReportAdminScreen.route}/{nroSolicitud}",
        arguments = listOf(navArgument("nroSolicitud") { type = NavType.StringType })
    ) { backStackEntry ->
        val nroSolicitud = backStackEntry.arguments?.getString("nroSolicitud") ?: ""
        TotalReportAdminScreen(modifier = modifier, navigationController = navController, nroSolicitud = nroSolicitud)
    }
}
