package com.optic.pramozventicoappz.presentation.screens.planes.seleccionarplanmode

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.optic.pramozventicoappz.domain.model.planes.BillingMode
import com.optic.pramozventicoappz.domain.model.planes.PlanType
import com.optic.pramozventicoappz.domain.util.Resource
import com.optic.pramozventicoappz.presentation.authstate.AuthStateVM
import com.optic.pramozventicoappz.presentation.screens.googleplaybilling.BillingProductUi
import com.optic.pramozventicoappz.presentation.screens.googleplaybilling.GooglePlayBillingManager
import com.optic.pramozventicoappz.presentation.screens.googleplaybilling.GooglePlayBillingViewModel
import com.optic.pramozventicoappz.presentation.screens.planes.seleccionarplanmode.components.BillingOptionCard
import com.optic.pramozventicoappz.presentation.screens.planes.seleccionarplanmode.components.PlanIncludesCard
import com.optic.pramozventicoappz.presentation.screens.planes.seleccionarplanmode.components.SelectPlanBottomBar
import com.optic.pramozventicoappz.presentation.ui.theme.BorderGray
import com.optic.pramozventicoappz.presentation.ui.theme.TextPrimary
import com.optic.pramozventicoappz.presentation.ui.theme.TextSecondary

@Composable
fun SelectPlanModeScreen(
    navController: NavHostController,
    googlePlayBillingViewModel: GooglePlayBillingViewModel,
    authStateVM: AuthStateVM = hiltViewModel()
) {
    val sessionData by authStateVM.sessionData.collectAsState()
    // DATOS DE LA SESSION
    val businessId = sessionData.businessId
    val email = sessionData.email
    val userId = sessionData.userId
    val planCode = sessionData.planCode



    val context = LocalContext.current
    val activity = context as Activity

    val selectedPlan by googlePlayBillingViewModel.selectedPlan.collectAsState()
    val selectedMode by googlePlayBillingViewModel.selectedBillingMode.collectAsState()
    val selectedProductId by googlePlayBillingViewModel.selectedProductId.collectAsState()
    val verifyPurchaseState by googlePlayBillingViewModel.verifyPurchaseState.collectAsState()

    val plan = selectedPlan ?: PlanType.PRO

    var billingProducts by remember {
        mutableStateOf<Map<String, BillingProductUi>>(emptyMap())
    }

    var billingError by remember {
        mutableStateOf<String?>(null)
    }

    val billingManager = remember {
        GooglePlayBillingManager(
            context = context,
            onPurchaseTokenReceived = { purchaseToken ->
                if (businessId != null) {
                    googlePlayBillingViewModel.verifySelectedPurchase(
                        purchaseToken  = purchaseToken,
                        businessId = businessId
                    )
                }
            },
            onBillingError = { message ->
                billingError = message
            },
            onProductsLoaded = { products ->
                billingProducts = products
            }
        )
    }

    LaunchedEffect(plan, selectedMode) {
        val productId = googlePlayBillingViewModel.buildProductId(plan, selectedMode)
        Log.d(
            "GP_BILLING_SCREEN",
            "Plan/mode activo | plan=$plan mode=$selectedMode productId=$productId"
        )
    }

    DisposableEffect(Unit) {
        billingManager.startConnection()

        onDispose {
            billingManager.endConnection()
        }
    }

    val primary = MaterialTheme.colorScheme.primary
    val background = MaterialTheme.colorScheme.background
    val surface = MaterialTheme.colorScheme.surface

    val accent = Color(0xFFFFC233)
    val accentSoft = Color(0xFFFFF6D8)
    val accentText = Color(0xFF8A6100)

    val monthlyPrice = when (plan) {
        PlanType.STANDARD -> 8.0
        PlanType.PRO -> 15.0
        PlanType.GOLD -> 35.0
    }

    val planTitle = when (plan) {
        PlanType.STANDARD -> "STANDARD"
        PlanType.PRO -> "PRO"
        PlanType.GOLD -> "GOLD"
    }

    val planBadge = when (plan) {
        PlanType.STANDARD -> "Más popular"
        PlanType.PRO -> "Para equipos"
        PlanType.GOLD -> "Premium"
    }

    val planTagline = when (plan) {
        PlanType.STANDARD -> "Todo lo que tu negocio necesita para arrancar fuerte."
        PlanType.PRO -> "Más poder para crecer sin límites de equipo."
        PlanType.GOLD -> "Escala sin límites. Para negocios grandes y ambiciosos."
    }

    val features = when (plan) {
        PlanType.STANDARD -> listOf(
            "Estadísticas completas",
            "Comparar meses, años y semanas",
            "Hasta 5 colaboradores",
            "Recibos personalizados con tu marca",
            "Exportar a Excel y PDF"
        )

        PlanType.PRO -> listOf(
            "Todo en Estándar",
            "Dashboard avanzado y top clientes",
            "Hasta 10 colaboradores",
            "Envío de recibos por WhatsApp",
            "Recordatorios automáticos",
            "Soporte prioritario"
        )

        PlanType.GOLD -> listOf(
            "Todo en Pro",
            "Estadísticas profesionales y Analytics",
            "Hasta 20 colaboradores",
            "Backup automático en la nube",
            "Multi negocio desde una cuenta",
            "Reportes inteligentes con IA"
        )
    }

    val annualBasePrice = monthlyPrice * 12
    val annualPrice = annualBasePrice * 0.70
    val annualMonthlyEquivalent = annualPrice / 12

    val monthlyBasePlanId = googlePlayBillingViewModel.buildProductId(
        plan = plan,
        mode = BillingMode.MONTHLY
    )

    val yearlyBasePlanId = googlePlayBillingViewModel.buildProductId(
        plan = plan,
        mode = BillingMode.YEARLY
    )

    val googleMonthlyPrice = monthlyBasePlanId
        ?.let { billingProducts[it]?.formattedPrice }
        ?.takeIf { it.isNotBlank() }

    val googleYearlyPrice = yearlyBasePlanId
        ?.let { billingProducts[it]?.formattedPrice }
        ?.takeIf { it.isNotBlank() }

    Scaffold(
        containerColor = background,
        topBar = {
            SelectPlanTopBar(
                navController = navController,
                planTitle = planTitle
            )
        },
        bottomBar = {
            SelectPlanBottomBar(
                selectedMode = selectedMode,
                monthlyPrice = monthlyPrice,
                annualPrice = annualPrice,
                annualMonthlyEquivalent = annualMonthlyEquivalent,
                primary = primary,
                surface = surface,
                onContinue = {
                    val basePlanId = selectedProductId

                    if (basePlanId.isNullOrBlank()) {
                        billingError = "No hay plan seleccionado"
                        return@SelectPlanBottomBar
                    }

                    Log.d(
                        "GP_BILLING_SCREEN",
                        "Comprar plan=$planTitle mode=$selectedMode basePlanId=$basePlanId"
                    )

                    billingManager.launchPurchase(
                        activity = activity,
                        basePlanId = basePlanId
                    )
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                top = 12.dp,
                bottom = 22.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                SelectedPlanHeader(
                    planTitle = planTitle,
                    planBadge = planBadge,
                    tagline = planTagline,
                    accent = accent,
                    accentSoft = accentSoft,
                    accentText = accentText
                )
            }

            item {
                Text(
                    text = "Elige cómo quieres pagar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                    letterSpacing = (-0.3).sp
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Puedes cambiar o cancelar tu plan cuando quieras.",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    BillingOptionCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(186.dp),
                        title = "Mensual",
                        subtitle = "Sin compromiso.",
                        price = googleMonthlyPrice ?: "$${"%.0f".format(monthlyPrice)}",
                        period = "usd / mes",
                        badge = null,
                        isSelected = selectedMode == BillingMode.MONTHLY,
                        primary = primary,
                        accent = accent,
                        accentSoft = accentSoft,
                        onClick = {
                            billingError = null
                            googlePlayBillingViewModel.selectBillingMode(BillingMode.MONTHLY)
                        }
                    )

                    BillingOptionCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(186.dp),
                        title = "Anual",
                        subtitle = "30% menos.",
                        price = googleYearlyPrice ?: "$${"%.0f".format(annualPrice)}",
                        period = "usd / año",
                        badge = "Ahorra",
                        helper = "$${"%.2f".format(annualMonthlyEquivalent)} / mes",
                        isSelected = selectedMode == BillingMode.YEARLY,
                        primary = primary,
                        accent = accent,
                        accentSoft = accentSoft,
                        onClick = {
                            billingError = null
                            googlePlayBillingViewModel.selectBillingMode(BillingMode.YEARLY)
                        }
                    )
                }
            }

            item {
                BillingStatusMessage(
                    verifyPurchaseState = verifyPurchaseState,
                    billingError = billingError
                )
            }

            item {
                PlanIncludesCard(
                    features = features,
                    accentSoft = accentSoft,
                    accentText = accentText,
                    surface = surface
                )
            }
        }
    }
}

@Composable
private fun BillingStatusMessage(
    verifyPurchaseState: Resource<*>,
    billingError: String?
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        when (verifyPurchaseState) {
            is Resource.Loading -> {
                Text(
                    text = "Verificando compra...",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            is Resource.Success -> {
                Text(
                    text = "Compra verificada correctamente",
                    color = Color(0xFF16A34A),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            is Resource.Failure -> {
                Text(
                    text = verifyPurchaseState.message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            else -> Unit
        }

        billingError?.let { message ->
            Spacer(Modifier.height(6.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectPlanTopBar(
    navController: NavHostController,
    planTitle: String
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextPrimary
                )
            }
        },
        title = {
            Text(
                text = "Plan $planTitle",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = TextPrimary,
            navigationIconContentColor = TextPrimary
        )
    )
}

@Composable
private fun SelectedPlanHeader(
    planTitle: String,
    planBadge: String,
    tagline: String,
    accent: Color,
    accentSoft: Color,
    accentText: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = Color.Black.copy(alpha = 0.035f),
                spotColor = Color.Black.copy(alpha = 0.06f)
            )
            .clip(RoundedCornerShape(26.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = BorderGray.copy(alpha = 0.65f),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(accentSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.WorkspacePremium,
                    contentDescription = null,
                    tint = accentText,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = planTitle,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Spacer(Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(accent.copy(alpha = 0.18f))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = planBadge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = accentText
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = tagline,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 18.sp
                )
            }
        }
    }
}