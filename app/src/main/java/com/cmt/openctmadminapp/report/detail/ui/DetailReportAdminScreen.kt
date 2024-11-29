package com.cmt.openctmadminapp.report.detail.ui

import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cmt.openctmadminapp.R
import com.cmt.openctmadminapp.core.navigation.Routes
import com.cmt.openctmadminapp.core.ui.form.ConfirmationDialog
import com.cmt.openctmadminapp.core.ui.header.FAB
import com.cmt.openctmadminapp.core.ui.header.HeaderSection
import com.cmt.openctmadminapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openctmadminapp.report.detail.data.network.response.SolicitudDTODetail
import com.cmt.openctmadminapp.report.detail.ui.viewmodel.DetailViewModel

@Composable
fun DetailReportAdminScreen(
    modifier: Modifier,
    navigationController: NavHostController,
    nroSolicitud: String,
    onThemeChange: (Int) -> Unit,
    detailViewModel: DetailViewModel = hiltViewModel(),
    onTypographyChange: (Typography) -> Unit,
) {
    val isLoading by detailViewModel.isLoading.collectAsState()
    val isActionLoading by detailViewModel.isActionLoading.collectAsState()
    val solicitudDetail by detailViewModel.solicitudDetail.collectAsState()

    LaunchedEffect(Unit) {
        detailViewModel.loadSolicitudDetail(nroSolicitud)
    }

    ConstraintLayout(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (header, detail, fold) = createRefs()

        HeaderSection(
            Modifier.constrainAs(header) { top.linkTo(parent.top) },
            true,
            navigationController
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(detail) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(fold.top)
                    height = Dimension.fillToConstraints
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (isLoading || isActionLoading) {
                LoadingScreen()
            } else {

                solicitudDetail?.let { detail ->
                    DetailReportContainer(
                        Modifier.fillMaxWidth(),
                        {
                            navigationController.navigate(
                                Routes.TotalReportAdminScreen.createRoute(
                                    detail.nroSolicitud
                                )
                            )
                        },
                        detail
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }


        FAB(
            isDarkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES,
            onThemeChange = onThemeChange,
            { },
            currentTypography = MaterialTheme.typography,
            onTypographyChange = onTypographyChange
        )

        ReportBoxBottom(
            Modifier
                .padding(top = 20.dp)
                .constrainAs(fold) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            detailViewModel,
            nroSolicitud
        ) {
            navigationController.navigate(Routes.ResearchAdminScreen.route)
        }
    }
}

@Composable
fun ReportBoxBottom(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel,
    nroSolicitud: String,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val solicitudDTODetail by detailViewModel.solicitudDetail.collectAsState()
    val isPending = solicitudDTODetail?.estado == "PENDIENTE"

    val statusColor = when (solicitudDTODetail?.estado) {
        "ACEPTADO" -> MaterialTheme.colorScheme.onSurface
        "RECHAZADO" -> MaterialTheme.colorScheme.surfaceVariant
        "PENDIENTE" -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.primary
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(170.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isPending) {
                Text(
                    text = stringResource(id = R.string.approve_report_description),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(25.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    // aprueba solicitud
                    MyButtonReport(
                        navigate = {
                            detailViewModel.approveSolicitud(nroSolicitud) { onNavigateBack() }
                            Toast.makeText(context, "Solicitud aprobada", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        },
                        textButton = stringResource(id = R.string.attend_report_button),
                        myIconButton = Icons.Default.Check
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    //rechazasolicitud
                    MyButtonReport(
                        navigate = {
                            detailViewModel.rejectSolicitud(nroSolicitud) { onNavigateBack() }
                            Toast.makeText(context, "Solicitud rechazada", Toast.LENGTH_SHORT)
                                .show()
                            onNavigateBack()
                        },
                        textButton = stringResource(id = R.string.reject_report_button),
                        myIconButton = Icons.Default.Clear
                    )

                }
            } else {
                Text(
                    text = "Solicitud resuelta",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Estado:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    solicitudDTODetail?.let {
                        Text(
                            text = it.estado,
                            color = statusColor,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyButtonReport(
    enable: Boolean = true,
    navigate: () -> Unit,
    textButton: String,
    myIconButton: ImageVector,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        ConfirmationDialog(
            onConfirm = {
                showDialog = false
                navigate()
            },
            onDismiss = { showDialog = false },
            text = "¿Está seguro de que desea $textButton?"
        )
    }

    Button(
        enabled = enable,
        onClick = {
            showDialog = true
            if (!enable) {
                Toast.makeText(context, "La solicitud ya fue atendida.", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.secondary,
            containerColor = MaterialTheme.colorScheme.onSecondary,
            disabledContentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.onErrorContainer,
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = textButton,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary
            )
            Icon(
                myIconButton,
                contentDescription = "navigate",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun DetailReportContainer(
    modifier: Modifier,
    navigate: () -> Unit,
    solicitudDTODetail: SolicitudDTODetail,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .verticalScroll(scrollState)
        ) {
            ReportHeader(
                solicitudDTODetail.nroSolicitud,
                solicitudDTODetail.fechaSolicitud,
                solicitudDTODetail.horaSolicitud
            )
            Spacer(modifier = Modifier.height(10.dp))
            ReportDetails(
                solicitudDTODetail.solicitante,
                solicitudDTODetail.identificacion,
                solicitudDTODetail.domicilio,
                solicitudDTODetail.correoElectronico,
                solicitudDTODetail.telefono,
                solicitudDTODetail.motivo,
                solicitudDTODetail.nroIncidente
            )
        }
        Icon(
            imageVector = Icons.Default.FileCopy,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .clickable { navigate() },
            tint = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun ReportDetails(
    applicant: String,
    id: String,
    address: String,
    email: String,
    phone: String,
    observers: String,
    nIncident: String,
) {
    MySection(stringResource(id = R.string.applicant_report))
    MySectionData(applicant)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.id_report))
    MySectionData(id)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.address_report))
    MySectionData(address)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.email_report))
    MySectionData(email)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.phone_report))
    MySectionData(phone)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.observ_report))
    MySectionData(observers)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.n_incident_report))
    MySectionData(nIncident)
}

@Composable
fun ReportHeader(incidentNumber: String, date: String, hour: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "Solicitud",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "N° $incidentNumber",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$date $hour",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun MySectionData(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.displaySmall,
        textAlign = TextAlign.Justify,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 1.dp)
            .padding(bottom = 5.dp)
    )
}

@Composable
fun MySection(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
}

