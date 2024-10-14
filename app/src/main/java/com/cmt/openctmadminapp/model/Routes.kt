package com.cmt.openctmadminapp.model

sealed class Routes(val route: String) {
    object HomeAdminScreen : Routes("homeAdminScreen")
    object LoginAdminScreen : Routes("loginAdminScreen")
    object ResearchAdminScreen : Routes("researchAdminScreen")
    object DetailReportAdminScreen : Routes("detailReportAdminScreen")
    object TotalReportAdminScreen: Routes("totalReportAdminScreen")
}