package com.cmt.openctmadminapp.core.navigation

sealed class Routes(val route: String) {
    object HomeAdminScreen : Routes("homeAdminScreen")
    object LoginAdminScreen : Routes("loginAdminScreen")
    object ResearchAdminScreen : Routes("researchAdminScreen")
    object DetailReportAdminScreen : Routes("detailReportAdminScreen") {
        fun createRoute(nroIncidente: String) = "detailReportAdminScreen/$nroIncidente"
    }
    object TotalReportAdminScreen: Routes("totalReportAdminScreen") {
        fun createRoute(nroIncidente: String) = "totalReportAdminScreen/$nroIncidente"
    }
}