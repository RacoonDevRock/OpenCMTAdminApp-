package com.cmt.openctmadminapp.core.navigation

sealed class Routes(val route: String) {
    object HomeAdminScreen : Routes("homeAdminScreen")
    object LoginAdminScreen : Routes("loginAdminScreen")
    object ResearchAdminScreen : Routes("researchAdminScreen")
    object DetailReportAdminScreen : Routes("detailReportAdminScreen") {
        fun createRoute(nroSolicitud: String) = "detailReportAdminScreen/$nroSolicitud"
    }
    object TotalReportAdminScreen: Routes("totalReportAdminScreen") {
        fun createRoute(nroSolicitud: String) = "totalReportAdminScreen/$nroSolicitud"
    }
}